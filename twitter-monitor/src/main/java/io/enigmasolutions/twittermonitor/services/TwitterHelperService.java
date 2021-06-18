package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.models.twitter.FollowsList;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.rest.TwitterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TwitterHelperService {
    private final TwitterConsumerRepository twitterConsumerRepository;

    private List<TwitterClient> twitterClients;
    private List<String> tweetsCache;

    @Autowired
    public TwitterHelperService(TwitterConsumerRepository twitterClientRepository) {
        this.twitterConsumerRepository = twitterClientRepository;
    }

    @PostConstruct
    public void initTwitterClients() {
        List<TwitterConsumer> consumers = twitterConsumerRepository.findAll();

        this.twitterClients = consumers.stream().map(TwitterClient::new).collect(Collectors.toList());
        this.tweetsCache = new LinkedList<>();
    }

    public User retrieveUser(String screenName) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("screen_name", screenName);
        TwitterClient currentClient = refreshClient();
        return Arrays.asList(currentClient.getUser(params).getBody()).get(0);
    }

    public FollowsList getFollowsList(String id) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", id);
        params.add("stringify_ids", "true");
        TwitterClient currentClient = refreshClient();
        return currentClient.getFollows(params).getBody();
    }

    public TwitterClient refreshClient() {
        TwitterClient client = twitterClients.remove(0);
        twitterClients.add(client);
        return client;
    }

    public Boolean isInTweetCache(String id) {
        if (tweetsCache.contains(id)) {
            return true;
        } else {
            tweetsCache.add(id);
            removeFromCache(id);
            return false;
        }
    }

    public void removeFromCache(String id) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                tweetsCache.remove(id);
            }
        };

        timer.schedule(timerTask, 60000);
    }
}
