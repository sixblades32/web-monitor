package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Component
public class HomeTimelineTwitterMonitor extends AbstractTwitterMonitor {

    private static final String TIMELINE_PATH = "statuses/home_timeline.json";

    @Autowired
    public HomeTimelineTwitterMonitor(TwitterScraperRepository twitterScraperRepository,
                                      TwitterHelperService twitterHelperService,
                                      KafkaTemplate<String,Tweet> kafkaTemplate) {
        super(4025, twitterScraperRepository, twitterHelperService, kafkaTemplate);
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
        params.add("tweet_mode", "extended");
        params.add("count", "1");
        params.add("include_entities", "1");
        params.add("include_user_entities", "1");

        return params;
    }




}
