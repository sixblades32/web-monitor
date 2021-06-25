package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.services.utils.TweetGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class UserTimelineMonitor extends AbstractTwitterMonitor {

    private final TwitterHelperService twitterHelperService;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TweetGenerator tweetGenerator;
    private final KafkaTemplate<String, Tweet> kafkaTemplate;
    private static final String TIMELINE_PATH = "statuses/user_timeline.json";

    private User user;

    public UserTimelineMonitor(
            TwitterScraperRepository twitterScraperRepository,
            TwitterHelperService twitterHelperService,
            TweetGenerator tweetGenerator,
            KafkaTemplate<String,Tweet> kafkaTemplate
    ) {
        super(700, twitterScraperRepository, twitterHelperService, kafkaTemplate, tweetGenerator);
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.tweetGenerator = tweetGenerator;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void start(String screenName) {
        user = twitterHelperService.retrieveUser(screenName);

        super.start();
    }

    @Override
    protected void executeTwitterMonitoring() {
        try {
            TweetResponse tweetResponse = getTweetResponse(getParams(), TIMELINE_PATH);
            sendTweet(tweetResponse);
        } catch (HttpClientErrorException exception) {

            if (exception.getStatusCode().value() >= 400 &&
                    exception.getStatusCode().value() < 500 &&
                    exception.getStatusCode().value() != 404) {
                if (failedCustomClients.size() > 15) {
                    stop();
                }
            }

            log.error(exception.toString());
        }
    }

    @Override
    protected MultiValueMap<String, String> generateParams(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", user.getId());
        params.add("tweet_mode", "extended");
        params.add("count", "${monitor.user-timeline.count}");
        params.add("include_entities", "1");
        params.add("include_user_entities", "1");

        return params;
    }
}
