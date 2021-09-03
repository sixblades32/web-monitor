package io.enigmasolutions.twittermonitor.services.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLTweet;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.QueryStringData;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.TweetLegacy;
import io.enigmasolutions.twittermonitor.models.twitter.v2.Tweet;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.enigmasolutions.twittermonitor.utils.TweetResponseGeneratorForGraphQL.generate;

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
    public MonitorStatus getMonitorStatus() {
        return MonitorStatus.builder()
                .status(super.getStatus())
                .user(user)
                .build();
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

            GraphQLTweet tweet = graphQlDataResponse.getData()
                    .getUser()
                    .getResult()
                    .getTimeline()
                    .getNestedTimeline()
                    .getInstructions().get(0)
                    .getEntries().get(0)
                    .getContent()
                    .getItemContent()
                    .getTweet();

            if (isTweetRelevant(tweet.getLegacy()) && !twitterHelperService.isTweetInGraphQLCache(tweet.getRestId()))
                tweetResponse = generate(tweet);
        }

        return tweetResponse;
    }

    private Boolean isTweetRelevant(TweetLegacy tweet) {
        return new Date().getTime() - Date.parse(tweet.getCreatedAt()) <= 25000;
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

    @Override
    protected void prepareClients(List<TwitterScraper> scrapers) {
        this.twitterCustomClients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());

        twitterCustomClients = Collections.synchronizedList(twitterCustomClients);
    }
}
