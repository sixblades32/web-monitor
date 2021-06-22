package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import org.springframework.stereotype.Component;

@Component
public class UserTimelineMonitor extends AbstractTwitterMonitor {

    private final TwitterHelperService twitterHelperService;

    private User user;

    public UserTimelineMonitor(
            TwitterHelperService twitterHelperService,
            TwitterScraperRepository twitterScraperRepository
    ) {
        super(5000, twitterScraperRepository);
        this.twitterHelperService = twitterHelperService;
    }

    public void start(String screenName) {
        user = twitterHelperService.retrieveUser(screenName);

        super.start();
    }

    @Override
    protected void executeTwitterMonitoring() {

    }

    @Override
    protected TweetResponse getTweetResponse() {
        return null;
    }

    @Override
    protected void sendTweet(TweetResponse tweetResponse) {

    }
}
