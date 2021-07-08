package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.broadcastmodels.TweetType;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.enigmasolutions.twittermonitor.utils.TweetGenerator.generate;

@Slf4j
public abstract class AbstractTwitterMonitor {

    private final KafkaProducer kafkaProducer;

    private final int timelineDelay;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TwitterHelperService twitterHelperService;
    private final List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors;
    private final List<ImageRecognitionProcessor> imageRecognitionProcessors;
    protected List<TwitterCustomClient> failedCustomClients = new LinkedList<>();
    private List<TwitterCustomClient> twitterCustomClients;

    private Status status = Status.STOPPED;
    private MultiValueMap<String, String> params;

    public AbstractTwitterMonitor(
            int timelineDelay,
            TwitterScraperRepository twitterScraperRepository,
            TwitterHelperService twitterHelperService,
            KafkaProducer kafkaProducer,
            List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
            List<ImageRecognitionProcessor> imageRecognitionProcessors
    ) {
        this.timelineDelay = timelineDelay;
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.kafkaProducer = kafkaProducer;
        this.plainTextRecognitionProcessors = plainTextRecognitionProcessors;
        this.imageRecognitionProcessors = imageRecognitionProcessors;
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
    }

    public Status getStatus() {
        return status;
    }

    protected abstract void executeTwitterMonitoring();
    protected abstract MultiValueMap<String, String> generateParams();

    protected TweetResponse getTweetResponse(MultiValueMap<String, String> params, String timelinePath) {
        TweetResponse tweetResponse = null;

        TwitterCustomClient currentClient = refreshClient();
        TweetResponse[] tweetResponseArray = currentClient
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

    private void runMonitor() {
        if (twitterCustomClients.isEmpty()) return;

        int delay = timelineDelay / twitterCustomClients.size();

        while (status == Status.RUNNING) {
            try {
                Thread.sleep(delay);
                executeTwitterMonitoring();
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
        CompletableFuture.runAsync(() -> processImageRecognition(tweetResponse, tweet.getImages()));
        CompletableFuture.runAsync(() ->
                processPlainTextRecognition(tweetResponse, tweetResponse.getEntities()
                        .getUrls().stream()
                        .map(Url::getExpandedUrl).collect(Collectors.toList()))
        );

        if (tweetResponse.getRetweetedStatus() != null){
            CompletableFuture.runAsync(() ->
                    processPlainTextRecognition(tweetResponse, tweetResponse.getRetweetedStatus().getEntities()
                            .getUrls().stream()
                            .map(Url::getExpandedUrl).collect(Collectors.toList()))
            );
        }else if(tweetResponse.getQuotedStatus() != null){
            CompletableFuture.runAsync(() ->
                    processPlainTextRecognition(tweetResponse, tweetResponse.getQuotedStatus().getEntities()
                            .getUrls().stream()
                            .map(Url::getExpandedUrl).collect(Collectors.toList()))
            );
        }
    }

    private void processPlainTextRecognition(TweetResponse tweetResponse, List<String> plainTextUrls) {

        String recognitionNestedUserName = getRecognitionNestedUserName(tweetResponse);

        plainTextRecognitionProcessors.stream()
                .parallel()
                .forEach(recognitionProcessor -> plainTextUrls.stream()
                        .parallel()
                        .forEach(url -> recognitionProcessingWrapper(recognitionProcessor, tweetResponse, url, recognitionNestedUserName)));
    }

    private void processImageRecognition(TweetResponse tweetResponse, List<String> imageUrls) {
        String recognitionNestedUserName = getRecognitionNestedUserName(tweetResponse);

        imageRecognitionProcessors.stream()
                .parallel()
                .forEach(recognitionProcessor -> imageUrls.stream()
                        .parallel()
                        .forEach(url -> recognitionProcessingWrapper(recognitionProcessor, tweetResponse, url, recognitionNestedUserName)));
    }

    private void recognitionProcessingWrapper(
            RecognitionProcessor recognitionProcessor,
            TweetResponse tweetResponse,
            String url,
            String recognitionNestedUserName
    ) {
        try {
            Recognition recognition = recognitionProcessor.processDataFromUrl(url);

            recognition.setTweetType(tweetResponse.getType());
            recognition.setUserName(tweetResponse.getUser().getScreenName());
            recognition.setNestedUserName(recognitionNestedUserName);

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

    private String getRecognitionNestedUserName(TweetResponse tweetResponse){
        if(tweetResponse.getType() == TweetType.RETWEET){
            if(tweetResponse.getRetweetedStatus() != null){
                return tweetResponse.getRetweetedStatus().getUser().getScreenName();
            }else if (tweetResponse.getQuotedStatus() != null){
                return tweetResponse.getQuotedStatus().getUser().getScreenName();
            }
        }else if(tweetResponse.getType() == TweetType.REPLY){
            return tweetResponse.getInReplyToScreenName();
        }

        return null;
    }
}
