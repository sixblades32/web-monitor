package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.broadcastmodels.Alert;
import io.enigmasolutions.broadcastmodels.BriefTweet;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.monitor.Status;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.Url;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.RecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import org.slf4j.Logger;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.enigmasolutions.twittermonitor.utils.TweetGenerator.buildBriefTweet;
import static io.enigmasolutions.twittermonitor.utils.TweetGenerator.generate;

public abstract class AbstractTwitterMonitor {

    private final KafkaProducer kafkaProducer;

    private final int timelineDelay;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TwitterHelperService twitterHelperService;
    private final Logger log;
    private final List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors;
    private final List<ImageRecognitionProcessor> imageRecognitionProcessors;
    protected List<TwitterCustomClient> failedCustomClients = Collections.synchronizedList(new LinkedList<>());

    private Status status = Status.STOPPED;
    private Integer delay = null;
    private MultiValueMap<String, String> params;
    private List<TwitterCustomClient> twitterCustomClients;

    public AbstractTwitterMonitor(
            int timelineDelay,
            TwitterScraperRepository twitterScraperRepository,
            TwitterHelperService twitterHelperService,
            KafkaProducer kafkaProducer,
            List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
            List<ImageRecognitionProcessor> imageRecognitionProcessors,
            Logger log
    ) {
        this.timelineDelay = timelineDelay;
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.kafkaProducer = kafkaProducer;
        this.plainTextRecognitionProcessors = plainTextRecognitionProcessors;
        this.imageRecognitionProcessors = imageRecognitionProcessors;
        this.log = log;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public void start() {
        synchronized (this) {
            if (status != Status.STOPPED) return;
            status = Status.RUNNING;
            params = generateParams();
            initTwitterCustomClients();
        }

        CompletableFuture.runAsync(this::runMonitor);
    }

    public void stop() {
        status = Status.STOPPED;
        failedCustomClients.clear();

        log.info("Monitor has been stopped");
    }

    public Status getStatus() {
        return status;
    }

    protected abstract void executeTwitterMonitoring();

    protected abstract MultiValueMap<String, String> generateParams();

    protected TweetResponse getTweetResponse(
            MultiValueMap<String, String> params,
            String timelinePath,
            TwitterCustomClient twitterCustomClient
    ) {
        TweetResponse tweetResponse = null;

        TweetResponse[] tweetResponseArray = twitterCustomClient
                .getBaseApiTimelineTweets(params, timelinePath)
                .getBody();

        if (tweetResponseArray != null && tweetResponseArray.length > 0) {
            tweetResponse = tweetResponseArray[0];
        }

        return tweetResponse;
    }

    protected void processTweetResponse(TweetResponse tweetResponse) {
        if (tweetResponse == null) return;

        if (!twitterHelperService.isInTweetCache(tweetResponse.getTweetId()) && isTweetRelevant(tweetResponse)) {
            Tweet tweet = generate(tweetResponse);

            log.info("Received tweet for processing: {}", tweet);

            CompletableFuture.runAsync(() -> tweetProcessing(tweetResponse, tweet));
            CompletableFuture.runAsync(() -> recognitionProcessing(tweetResponse, tweet));
        }
    }

    protected void processErrorResponse(HttpClientErrorException exception, TwitterCustomClient twitterCustomClient) {

        log.error(exception.toString());

        if (exception.getStatusCode().value() >= 400 &&
                exception.getStatusCode().value() < 500 &&
                exception.getStatusCode().value() != 404) {

            reshuffleClients(twitterCustomClient);
            processRateLimitError(exception, twitterCustomClient);
            calculateDelay();

            CompletableFuture.runAsync(() -> processAlertTarget(exception, twitterCustomClient));

            if (failedCustomClients.size() > 15) {
                stop();
            }
        }
    }

    private void reshuffleClients(TwitterCustomClient twitterCustomClient) {

        twitterCustomClients.remove(twitterCustomClient);
        failedCustomClients.add(twitterCustomClient);
    }

    private void processRateLimitError(HttpClientErrorException exception, TwitterCustomClient twitterCustomClient) {

        if (exception.getStatusCode().value() == 429) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    restoreFailedClient(twitterCustomClient);
                }
            };

            timer.schedule(timerTask, 60000);
        }
    }

    private void calculateDelay() {
        delay = timelineDelay / twitterCustomClients.size();
    }

    private void restoreFailedClient(TwitterCustomClient twitterCustomClient) {
        if (!failedCustomClients.contains(twitterCustomClient)) return;

        failedCustomClients.remove(twitterCustomClient);
        twitterCustomClients.add(twitterCustomClient);
        log.info("Scraper " + twitterCustomClient.getTwitterScraper().getId() + " successfully restored!");
    }

    private void processAlertTarget(HttpClientErrorException exception, TwitterCustomClient twitterCustomClient) {

        Alert alert = Alert.builder()
                .failedMonitorId(twitterCustomClient.getTwitterScraper().getTwitterUser().getTwitterId())
                .failedMonitorsCount(failedCustomClients.size())
                .validMonitorsCount(twitterCustomClients.size())
                .reason(exception.getMessage())
                .build();

        kafkaProducer.sentAlertBroadcast(alert);
    }

    private void runMonitor() {
        if (twitterCustomClients.isEmpty()) return;

        calculateDelay();

        log.info("Monitor has been started");

        while (status == Status.RUNNING) {
            try {
                Thread.sleep(delay);
                CompletableFuture.runAsync(this::executeTwitterMonitoring);
            } catch (Exception exception) {
                log.error("Unknown exception", exception);
            }

        }
    }

    private void initTwitterCustomClients() {
        List<TwitterScraper> scrapers = twitterScraperRepository.findAll();

        this.twitterCustomClients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());

        twitterCustomClients = Collections.synchronizedList(twitterCustomClients);
    }

    private Boolean isTweetRelevant(TweetResponse tweetResponse) {
        return new Date().getTime() - Date.parse(tweetResponse.getCreatedAt()) <= 25000;
    }

    protected synchronized TwitterCustomClient refreshClient() {
        TwitterCustomClient client = twitterCustomClients.remove(0);
        twitterCustomClients.add(client);

        return client;
    }

    private void tweetProcessing(TweetResponse tweetResponse, Tweet tweet) {
        CompletableFuture.runAsync(() -> processCommonTarget(tweetResponse, tweet));
        CompletableFuture.runAsync(() -> processLiveReleaseTarget(tweetResponse, tweet));
    }

    private void recognitionProcessing(TweetResponse tweetResponse, Tweet tweet) {
        BriefTweet briefTweet = buildBriefTweet(tweetResponse);

        CompletableFuture.runAsync(() ->
                processImageRecognition(tweetResponse, briefTweet, tweet.getImages()));
        CompletableFuture.runAsync(() ->
                processPlainTextRecognition(tweetResponse, briefTweet, null, tweetResponse.getEntities()
                        .getUrls().stream()
                        .map(Url::getExpandedUrl)
                        .collect(Collectors.toList()))
        );

        if (tweetResponse.getRetweetedStatus() != null) {
            BriefTweet nestedBriefTweet = buildBriefTweet(tweetResponse.getRetweetedStatus());

            CompletableFuture.runAsync(() ->
                    processPlainTextRecognition(tweetResponse, briefTweet, nestedBriefTweet, tweetResponse
                            .getRetweetedStatus()
                            .getEntities()
                            .getUrls().stream()
                            .map(Url::getExpandedUrl)
                            .collect(Collectors.toList()))
            );
        } else if (tweetResponse.getQuotedStatus() != null) {
            BriefTweet nestedBriefTweet = buildBriefTweet(tweetResponse.getRetweetedStatus());

            CompletableFuture.runAsync(() ->
                    processPlainTextRecognition(tweetResponse, briefTweet, nestedBriefTweet, tweetResponse
                            .getQuotedStatus()
                            .getEntities()
                            .getUrls().stream()
                            .map(Url::getExpandedUrl)
                            .collect(Collectors.toList()))
            );
        }
    }

    private void processPlainTextRecognition(
            TweetResponse tweetResponse,
            BriefTweet briefTweet,
            BriefTweet nestedBriefTweet,
            List<String> plainTextUrls
    ) {
        plainTextRecognitionProcessors.stream()
                .parallel()
                .forEach(recognitionProcessor -> plainTextUrls.stream()
                        .parallel()
                        .forEach(url -> recognitionProcessingWrapper(
                                recognitionProcessor,
                                tweetResponse,
                                url,
                                briefTweet,
                                nestedBriefTweet)
                        )
                );
    }

    private void processImageRecognition(
            TweetResponse tweetResponse,
            BriefTweet briefTweet,
            List<String> imageUrls
    ) {
        imageRecognitionProcessors.stream()
                .parallel()
                .forEach(recognitionProcessor -> imageUrls.stream()
                        .parallel()
                        .forEach(url -> recognitionProcessingWrapper(
                                recognitionProcessor,
                                tweetResponse,
                                url,
                                briefTweet,
                                null)
                        )
                );
    }

    private void recognitionProcessingWrapper(
            RecognitionProcessor recognitionProcessor,
            TweetResponse tweetResponse,
            String url,
            BriefTweet briefTweet,
            BriefTweet nestedBriefTweet
    ) {
        try {
            Recognition recognition = recognitionProcessor.processDataFromUrl(url);

            recognition.setTweetType(tweetResponse.getType());
            recognition.setBriefTweet(briefTweet);
            recognition.setNestedBriefTweet(nestedBriefTweet);

            CompletableFuture.runAsync(() -> processCommonTargetRecognition(tweetResponse, recognition));
            CompletableFuture.runAsync(() -> processLiveReleaseTargetRecognition(tweetResponse, recognition));
        } catch (Exception e) {
            log.error("Exception received", e);
        }
    }

    private void processCommonTarget(TweetResponse tweetResponse, Tweet tweet) {
        if (isCommonTargetValid(tweetResponse)) {
            kafkaProducer.sendTweetToBaseBroadcast(tweet);
        }
    }

    private void processLiveReleaseTarget(TweetResponse tweetResponse, Tweet tweet) {
        if (isLiveReleaseTargetValid(tweetResponse)) {
            kafkaProducer.sendTweetLiveReleaseBroadcast(tweet);
        }
    }

    private void processCommonTargetRecognition(TweetResponse tweetResponse, Recognition recognition) {
        if (isCommonTargetValid(tweetResponse)) {
            kafkaProducer.sendTweetToBaseBroadcastRecognition(recognition);
        }
    }

    private void processLiveReleaseTargetRecognition(TweetResponse tweetResponse, Recognition recognition) {
        if (isLiveReleaseTargetValid(tweetResponse)) {
            kafkaProducer.sendTweetLiveReleaseRecognition(recognition);
        }
    }

    private Boolean isCommonTargetValid(TweetResponse tweetResponse) {
        String targetId = tweetResponse.getUser().getId();

        return twitterHelperService.checkCommonPass(targetId);
    }

    private Boolean isLiveReleaseTargetValid(TweetResponse tweetResponse) {
        String targetId = tweetResponse.getUser().getId();

        return twitterHelperService.checkLiveReleasePass(targetId);
    }
}
