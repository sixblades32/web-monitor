package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;

import java.util.Date;
import java.util.List;

// TODO: насколько я помню в Java 11, можно сделать через Interface

public abstract class AbstractTwitterMonitor {

    protected List<TwitterCustomClient> twitterCustomClients;

    public AbstractTwitterMonitor(List<TwitterCustomClient> twitterCustomClients) {
        this.twitterCustomClients = twitterCustomClients;
    }

    public abstract void start();

    public abstract void stop();

    public abstract TweetResponse getTweet();

    public abstract void sendTweet(Tweet tweet);

    public Boolean isTweetRelevant(TweetResponse tweetResponse) {

        return new Date().getTime() - Date.parse(tweetResponse.getCreatedAt()) <= 25000;
    }

    public synchronized TwitterCustomClient refreshClient() {
        TwitterCustomClient client = twitterCustomClients.remove(0);
        twitterCustomClients.add(client);
        return client;
    }
}
