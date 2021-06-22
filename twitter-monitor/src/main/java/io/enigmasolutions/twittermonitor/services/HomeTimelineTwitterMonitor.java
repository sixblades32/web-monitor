package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.services.utils.TweetGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HomeTimelineTwitterMonitor extends AbstractTwitterMonitor {

    private final TwitterScraperRepository twitterScraperRepository;
    private List<TwitterCustomClient> twitterCustomClients;
    private final TweetGenerator tweetGenerator;
    private final TwitterHelperService twitterHelperService;
    private final KafkaTemplate<String, Tweet> kafkaTemplate;

    @Autowired
    public HomeTimelineTwitterMonitor(TwitterScraperRepository twitterScraperRepository,
                                      TwitterHelperService twitterHelperService,
                                      TweetGenerator tweetGenerator,
                                      KafkaTemplate<String,Tweet> kafkaTemplate) {
        super(4025, twitterScraperRepository);
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.tweetGenerator = tweetGenerator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initTwitterCustomClients() {
        List<TwitterScraper> scrapers = twitterScraperRepository.findAll();

        this.twitterCustomClients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());
    }

    @Override
    protected void executeTwitterMonitoring() {
        try {
            TweetResponse tweetResponse = getTweetResponse();
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
    protected TweetResponse getTweetResponse() {
        TweetResponse tweetResponse = null;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tweet_mode", "extended");
        params.add("count", "1");
        params.add("include_entities", "1");
        params.add("include_user_entities", "1");

        TwitterCustomClient currentClient = refreshClient();
        TweetResponse[] tweetResponseArray = currentClient
                .getHomeTimelineTweets(params)
                .getBody();

        if (tweetResponseArray != null && tweetResponseArray.length > 0) {
            tweetResponse = tweetResponseArray[0];
        }

        return tweetResponse;
    }

    @Override
    protected void sendTweet(TweetResponse tweetResponse) {
        if (!twitterHelperService.isInTweetCache(tweetResponse.getTweetId()) && isTweetRelevant(tweetResponse)) {
            Tweet tweet = tweetGenerator.generate(tweetResponse);
            kafkaTemplate.send("twitter-tweet-broadcast", tweet);
        }
    }

}
