package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserTimelineMonitor extends AbstractTwitterMonitor {

    private static final String TIMELINE_PATH = "statuses/user_timeline.json";

    private final User user;

    public UserTimelineMonitor(
            TwitterScraperRepository twitterScraperRepository,
            TwitterHelperService twitterHelperService,
            KafkaProducer kafkaProducer,
            List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
            List<ImageRecognitionProcessor> imageRecognitionProcessors, User user
    ) {
        super(
                700,
                twitterScraperRepository,
                twitterHelperService,
                kafkaProducer,
                plainTextRecognitionProcessors,
                imageRecognitionProcessors,
                log);

        this.user = user;
    }

    public void start() {
        log.info("Current monitor user is: {}", user);

        super.start();
    }

    public void stop() {
        super.stop();

        log.info("User: {}", user);
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
            TweetResponse tweetResponse = getTweetResponse(getParams(), TIMELINE_PATH, currentClient);
            processTweetResponse(tweetResponse);
        } catch (HttpClientErrorException exception) {
            processErrorResponse(exception, currentClient);
        }
    }

    @Override
    protected MultiValueMap<String, String> generateParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", user.getId());
        params.add("tweet_mode", "extended");
        params.add("count", "1");
        params.add("include_entities", "1");
        params.add("include_user_entities", "1");

        return params;
    }

    @Override
    protected void prepareClients(List<TwitterScraper> scrapers) {
        this.twitterCustomClients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());

        twitterCustomClients = Collections.synchronizedList(twitterCustomClients);
    }

    public User getUser() {
        return user;
    }
}
