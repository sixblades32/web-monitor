package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.monitor.Status;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractTwitterMonitor {

    private final int timelineDelay;
    private final TwitterScraperRepository twitterScraperRepository;

    protected List<TwitterCustomClient> twitterCustomClients;
    protected List<TwitterCustomClient> failedCustomClients;

    private Status status = Status.STOPPED;

    public AbstractTwitterMonitor(int timelineDelay, TwitterScraperRepository twitterScraperRepository) {
        this.timelineDelay = timelineDelay;
        this.twitterScraperRepository = twitterScraperRepository;
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

    protected abstract TweetResponse getTweetResponse();

    protected abstract void sendTweet(TweetResponse tweetResponse);

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
