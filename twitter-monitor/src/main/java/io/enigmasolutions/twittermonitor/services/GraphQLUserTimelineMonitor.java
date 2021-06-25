package io.enigmasolutions.twittermonitor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.graphQL.Data;
import io.enigmasolutions.twittermonitor.models.twitter.graphQL.QueryStringData;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.services.utils.BaseTweetResponseGenerator;
import io.enigmasolutions.twittermonitor.services.utils.TweetGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GraphQLUserTimelineMonitor extends AbstractTwitterMonitor{
    private final TwitterScraperRepository twitterScraperRepository;
    private final BaseTweetResponseGenerator baseTweetResponseGenerator;
    private final TweetGenerator tweetGenerator;
    private final TwitterHelperService twitterHelperService;
    private final KafkaTemplate<String, Tweet> kafkaTemplate;

    private User user;

    public GraphQLUserTimelineMonitor(
            TwitterScraperRepository twitterScraperRepository,
            BaseTweetResponseGenerator baseTweetResponseGenerator,
            TwitterHelperService twitterHelperService,
            TweetGenerator tweetGenerator,
            KafkaTemplate<String,Tweet> kafkaTemplate
    ) {
        super(1900, twitterScraperRepository, twitterHelperService, kafkaTemplate, tweetGenerator);
        this.twitterScraperRepository = twitterScraperRepository;
        this.baseTweetResponseGenerator = baseTweetResponseGenerator;
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
            TweetResponse tweetResponse = getTweetResponse();

            sendTweet(tweetResponse);
        } catch (HttpClientErrorException exception) {

//            if (exception.getStatusCode().value() >= 400 &&
//                    exception.getStatusCode().value() < 500 &&
//                    exception.getStatusCode().value() != 404) {
//                if (failedCustomClients.size() > 15) {
//                    stop();
//                }
//            }

            log.error(exception.toString());
        }
    }

    private TweetResponse getTweetResponse(){
        TweetResponse tweetResponse = null;


        TwitterCustomClient currentClient = refreshClient();
        Data graphQlDataResponse = currentClient
                .getGraphQLApiTimelineTweets(getParams())
                .getBody();

        System.out.println(graphQlDataResponse);

        if (graphQlDataResponse != null) {
            tweetResponse = baseTweetResponseGenerator.generate(graphQlDataResponse.getUser()
                    .getResult()
                    .getTimeline()
                    .getNestedTimeline()
                    .getInstructions().get(0)
                    .getEntries().get(0)
                    .getContent()
                    .getItemContent()
                    .getTweet());
        }

        return tweetResponse;
    }

    @Override
    protected MultiValueMap<String, String> generateParams(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String variables = null;

        QueryStringData data = QueryStringData.builder()
                .userId(user.getId())
                .count(1)
                .withHighlightedLabel(true)
                .withTweetQuoteCount(true)
                .includePromotedContent(true)
                .withTweetResult(false)
                .withUserResults(false)
                .withVoice(false)
                .withNonLegacyCard(true)
                .withBirdwatchPivots(false)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            variables = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        params.add("variables", UriUtils.encode(variables, StandardCharsets.UTF_8));

        return params;
    }
}
