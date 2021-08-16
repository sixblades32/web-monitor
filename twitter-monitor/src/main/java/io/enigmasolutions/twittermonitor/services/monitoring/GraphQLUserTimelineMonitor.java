package io.enigmasolutions.twittermonitor.services.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.QueryStringData;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static io.enigmasolutions.twittermonitor.utils.TweetResponseGenerator.generate;

@Slf4j
@Component
public class GraphQLUserTimelineMonitor extends AbstractTwitterMonitor {

    private final TwitterHelperService twitterHelperService;

    private User user;

    public GraphQLUserTimelineMonitor(
            TwitterScraperRepository twitterScraperRepository,
            TwitterHelperService twitterHelperService,
            KafkaProducer kafkaProducer,
            List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
            List<ImageRecognitionProcessor> imageRecognitionProcessors
    ) {
        super(
                1900,
                twitterScraperRepository,
                twitterHelperService,
                kafkaProducer,
                plainTextRecognitionProcessors,
                imageRecognitionProcessors,
                log);
        this.twitterHelperService = twitterHelperService;
    }

    public void start(String screenName) {
        try {
            user = twitterHelperService.retrieveUser(screenName);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoTwitterUserMatchesException();
            }
        }

        log.info("Current monitor user is: {}", user);

        super.start();
    }

    @Override
    protected void executeTwitterMonitoring() {
        TwitterCustomClient currentClient = refreshClient();

        try {
            TweetResponse tweetResponse = getTweetResponse(currentClient);
            processTweetResponse(tweetResponse);
        } catch (HttpClientErrorException exception) {
            processErrorResponse(exception, currentClient);
        }
    }

    private TweetResponse getTweetResponse(TwitterCustomClient currentClient) {
        TweetResponse tweetResponse = null;

        GraphQLResponse graphQlDataResponse = currentClient
                .getGraphQLApiTimelineTweets(getParams())
                .getBody();

        if (graphQlDataResponse != null && !graphQlDataResponse.getData()
                .getUser()
                .getResult()
                .getTimeline()
                .getNestedTimeline()
                .getInstructions().isEmpty()) {
            tweetResponse = generate(graphQlDataResponse.getData()
                    .getUser()
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
    protected MultiValueMap<String, String> generateParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String variables = null;

        QueryStringData data = QueryStringData.builder()
                .userId(user.getId())
                .count(1)
                .withHighlightedLabel(true)
                .withTweetQuoteCount(true)
                .includePromotedContent(true)
                .withTweetResult(false)
                .withCommunity(false)
                .withReactions(false)
                .withSuperFollowsTweetFields(false)
                .withSuperFollowsUserFields(false)
                .withUserResults(false)
                .withVoice(false)
                .withBirdwatchPivots(false)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            variables = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        params.add("variables", variables);

        return params;
    }
}
