package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.monitor.Status;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.services.utils.TweetGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractTwitterMonitor {

    private final int timelineDelay;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TwitterHelperService twitterHelperService;
    private final KafkaTemplate<String, Tweet> kafkaTemplate;
    private final TweetGenerator tweetGenerator;

    protected List<TwitterCustomClient> twitterCustomClients;
    protected List<TwitterCustomClient> failedCustomClients;

    private Status status = Status.STOPPED;
    private MultiValueMap<String, String> params;

    public AbstractTwitterMonitor(int timelineDelay,
                                  TwitterScraperRepository twitterScraperRepository,
                                  TwitterHelperService twitterHelperService,
                                  KafkaTemplate<String,Tweet> kafkaTemplate,
                                  TweetGenerator tweetGenerator) {
        this.timelineDelay = timelineDelay;
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.kafkaTemplate = kafkaTemplate;
        this.tweetGenerator = tweetGenerator;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    @PostConstruct
    public void initTwitterCustomClients() {
        List<TwitterScraper> scrapers = twitterScraperRepository.findAll();

        this.twitterCustomClients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());
    }

    public void start() {
        synchronized (this) {
            if (status != Status.STOPPED) return;
            status = Status.RUNNING;
            params = generateParams();
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

    protected TweetResponse getTweetResponse(MultiValueMap<String, String> params, String timelinePath){
        TweetResponse tweetResponse = null;

        TwitterCustomClient currentClient = refreshClient();
        TweetResponse[] tweetResponseArray = currentClient
                .getBaseApiTimelineTweets(params, timelinePath)
                .getBody();

        if (tweetResponseArray != null && tweetResponseArray.length > 0) {
            tweetResponse = tweetResponseArray[0];
        }

        return tweetResponse;
    };

    protected void sendTweet(TweetResponse tweetResponse){
        if (!twitterHelperService.isInTweetCache(tweetResponse.getTweetId()) && isTweetRelevant(tweetResponse)) {
            Tweet tweet = tweetGenerator.generate(tweetResponse);
            kafkaTemplate.send("twitter-tweet-broadcast", tweet);
        }
    };

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

    protected Boolean isTweetRelevant(TweetResponse tweetResponse) {
        return new Date().getTime() - Date.parse(tweetResponse.getCreatedAt()) <= 25000;
    }

    protected synchronized TwitterCustomClient refreshClient() {
        TwitterCustomClient client = twitterCustomClients.remove(0);
        twitterCustomClients.add(client);

        return client;
    }
}
