package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.models.broadcast.BroadcastTweet;
import io.enigmasolutions.twittermonitor.models.twitter.base.Tweet;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;

import java.util.Date;
import java.util.List;

// TODO: насколько я помню в Java 11, можно сделать через Interface

public abstract class AbstractMonitor {

    public abstract void start();

    public abstract void stop();

    public abstract Tweet getTweet();

    public abstract void sendTweet(BroadcastTweet tweet);

    public Boolean isTweetRelevant(Tweet tweet) {
        Boolean relevance = new Date().getTime() - Date.parse(tweet.getCreatedAt()) <= 25000;
        return relevance;
    }

    public TwitterCustomClient refreshClient(List<TwitterCustomClient> twitterCustomClients) {
        TwitterCustomClient client = twitterCustomClients.remove(0);
        twitterCustomClients.add(client);
        return client;
    }
}
