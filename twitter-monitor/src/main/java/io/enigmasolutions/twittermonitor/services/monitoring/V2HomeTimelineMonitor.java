package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.v2.GlobalObjects;
import io.enigmasolutions.twittermonitor.models.twitter.v2.Tweet;
import io.enigmasolutions.twittermonitor.models.twitter.v2.V2Response;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.enigmasolutions.twittermonitor.utils.TweetResponseGeneratorForV2.generateV2;

@Slf4j
@Component
public class V2HomeTimelineMonitor extends AbstractTwitterMonitor {

    private static final String TIMELINE_PATH = "timeline/home.json";

    public V2HomeTimelineMonitor(TwitterScraperRepository twitterScraperRepository,
                                 TwitterHelperService twitterHelperService,
                                 KafkaProducer kafkaProducer,
                                 List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
                                 List<ImageRecognitionProcessor> imageRecognitionProcessors) {
        super(1006,
                twitterScraperRepository,
                twitterHelperService,
                kafkaProducer,
                plainTextRecognitionProcessors,
                imageRecognitionProcessors,
                log);
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

        V2Response v2Response = currentClient
                .getV2BaseTimelineTweets(getParams(), TIMELINE_PATH)
                .getBody();

        if (v2Response != null && !v2Response.getGlobalObjects().getTweets().isEmpty()) {
            GlobalObjects globalObjects = v2Response.getGlobalObjects();
            ArrayList<Tweet> tweets = new ArrayList<>(globalObjects.getTweets().values());
            ArrayList<User> users = new ArrayList<>(globalObjects.getUsers().values());

            tweetResponse = generateV2(tweets, users, TweetResponse.builder());
        }

        return tweetResponse;
    }

    @Override
    protected MultiValueMap<String, String> generateParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("include_profile_interstitial_type", "1");
        params.add("include_blocking", "1");
        params.add("include_blocked_by", "1");
        params.add("include_followed_by", "1");
        params.add("include_want_retweets", "1");
        params.add("include_mute_edge", "1");
        params.add("include_can_dm", "1");
        params.add("include_can_media_tag", "1");
        params.add("skip_status", "1");
        params.add("include_cards", "1");
        params.add("include_ext_alt_text", "true");
        params.add("include_quote_count", "true");
        params.add("include_reply_count", "true");
        params.add("tweet_mode", "extended");
        params.add("include_entities", "true");
        params.add("include_user_entities", "true");
        params.add("include_ext_media_color", "true");
        params.add("include_ext_media_availability", "true");
        params.add("simple_quoted_tweet", "true");
        params.add("earned", "1");
        params.add("count", "1");

        return params;
    }

    @Override
    protected void prepareClients(List<TwitterScraper> scrapers) {

        List<TwitterScraper> invalidScrapers = new ArrayList<>();
        log.info("Preparing started");

        for(TwitterScraper twitterScraper: scrapers){
            String scraperTwitterId = twitterScraper.getTwitterUser().getTwitterId();
            if(scraperTwitterId.equals("1371445090401542147") || scraperTwitterId.equals("1377556119808249859") || scraperTwitterId.equals("1376524710616432648")){
                invalidScrapers.add(twitterScraper);
                log.info(scraperTwitterId + " scraper not loaded in Twitter Custom Clients Pool!");
            }
        }

        invalidScrapers.forEach(scrapers::remove);

        List<TwitterCustomClient> clients = scrapers.stream()
                .map(TwitterCustomClient::new)
                .collect(Collectors.toList());

        twitterCustomClients = Collections.synchronizedList(clients);
    }
}
