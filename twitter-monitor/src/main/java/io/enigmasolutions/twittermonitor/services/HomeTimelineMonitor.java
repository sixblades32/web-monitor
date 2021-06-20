package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.broadcast.BroadcastTweet;
import io.enigmasolutions.twittermonitor.models.twitter.base.Tweet;
import io.enigmasolutions.twittermonitor.services.rest.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.services.utils.BroadcastTweetFromBaseGenerator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HomeTimelineMonitor extends AbstractMonitor {

    private final TwitterScraperRepository twitterScraperRepository;
    private List<TwitterCustomClient> twitterCustomClients;
    private final BroadcastTweetFromBaseGenerator broadcastTweetFromBaseGenerator;
    private final TwitterHelperService twitterHelperService;
    private final KafkaTemplate<String, BroadcastTweet> kafkaTemplate;

    private Boolean isRunning = false;
    // TODO: нужно static
    private final int HOME_TIMELINE_DELAY = 4025;
    // TODO: где происходит наполнение, Clietns орфографическая ошибка
    private List<TwitterCustomClient> failedCustomClietns;
    // TODO: все статусы надо вынести в общий Enum, скорее всего они общие для всей системы, isRunning можно убрать
    private String status = "Stopped";
    // TODO: можно сделать локальной переменной
    private int delay;


    // TODO: читабельный вид
    @Autowired
    public HomeTimelineMonitor(TwitterScraperRepository twitterScraperRepository,
                               TwitterHelperService twitterHelperService,
                               BroadcastTweetFromBaseGenerator broadcastTweetFromBaseGenerator, KafkaTemplate<String,BroadcastTweet> kafkaTemplate) {
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.broadcastTweetFromBaseGenerator = broadcastTweetFromBaseGenerator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initTwitterCustomClients(){
        List<TwitterScraper> scrapers = twitterScraperRepository.findAll();

        this.twitterCustomClients = scrapers.stream().map(TwitterCustomClient::new).collect(Collectors.toList());
    }

    @Override
    public void start() throws HttpClientErrorException {

        isRunning = true;
        status = "Running";

        delay = HOME_TIMELINE_DELAY / twitterCustomClients.size();

        while (isRunning){
            try {
                Tweet tweet = getTweet();

                Thread.sleep(delay);

                // TODO: давай это тоже сделаем отдельным методом
                if(!twitterHelperService.isInTweetCache(tweet.getTweetId()) && isTweetRelevant(tweet)){
                    BroadcastTweet broadcastTweet = broadcastTweetFromBaseGenerator.generate(tweet);

                    sendTweet(broadcastTweet);
                }
            }catch (HttpClientErrorException exception){
                // TODO: разве есть 600 ошибки?)
                if(exception.getStatusCode().value() < 500 || exception.getStatusCode().value() >= 600){
                    log.error(exception.toString());
                }else if(exception.getStatusCode().value() >= 400 &&
                        exception.getStatusCode().value() < 500 &&
                        exception.getStatusCode().value() != 404){
                    if(failedCustomClietns.size() > 15){
                        stop();
                    }

                    log.error(exception.toString());
                }
                // TODO: лог, не System.out.println
                System.out.println(exception.getStatusCode());
            } catch (InterruptedException exception) {
                log.error(exception.toString());
            }

        }

    }

    @Override
    public void stop() {
        isRunning = false;
        status = "Stopped";
    }

    // TODO: Давай уберем @NonNull и сделаем врапперы, кол-во сделаем опциональным, через параметр
    @NonNull
    public Tweet getTweet(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tweet_mode", "extended");
        params.add("count", "1");
        params.add("include_entities", "1");
        params.add("include_user_entities", "1");

        TwitterCustomClient currentClient = refreshClient(twitterCustomClients);

        return Arrays.asList(currentClient.getHomeTimelineTweets(params).getBody()).get(0);
    }

    public void sendTweet(BroadcastTweet tweet){
        kafkaTemplate.send("twitter-tweet-broadcast", tweet);
        System.out.println(tweet);
    }


}
