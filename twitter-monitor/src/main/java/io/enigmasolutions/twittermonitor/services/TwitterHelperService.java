package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.Target;
import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.models.twitter.FollowsList;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.rest.TwitterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Service
public class TwitterHelperService {
    private final TwitterConsumerRepository twitterConsumerRepository;
    private final TargetRepository targetRepository;

    private List<TwitterClient> twitterClients;
    private List<String> commonTargetIds;
    public List<String> advancedTargetIds = new LinkedList<>();
    private List<String> tweetsCache;

    @Autowired
    public TwitterHelperService(TwitterConsumerRepository twitterClientRepository, TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
        this.twitterConsumerRepository = twitterClientRepository;
    }

    @PostConstruct
    public void init(){
        initTargets();
        initTwitterClients();
        this.tweetsCache = new LinkedList<>();
    }

    private void initTargets() {
        List<Target> targets = targetRepository.findAll();

        commonTargetIds = targets.stream()
                .map(Target::getIdentifier)
                .collect(Collectors.toList());
    }

    public void initTwitterClients() {
        List<TwitterConsumer> consumers = twitterConsumerRepository.findAll();

        this.twitterClients = consumers.stream()
                .map(TwitterClient::new)
                .collect(Collectors.toList());
    }

    public Boolean checkCommonPass(String id) {
        return commonTargetIds.contains(id);
    }

    public Boolean checkLiveReleasePass(String id) {
        return advancedTargetIds.contains(id);
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
