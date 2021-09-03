package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.common.FollowsList;
import io.enigmasolutions.twittermonitor.services.web.TwitterRegularClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Service
public class TwitterHelperService {

    private final List<String> liveReleaseTargetsIds = new LinkedList<>();
    private final List<String> liveReleaseTargetsScreenNames = new LinkedList<>();
    private final List<String> tweetsCache = new LinkedList<>();
    private final List<String> v2TweetsCache = new LinkedList<>();
    private final List<String> graphQLTweetsCache = new LinkedList<>();

    private final TwitterConsumerRepository twitterConsumerRepository;
    private final TargetRepository targetRepository;

    private List<TwitterRegularClient> twitterRegularClients;
    private List<String> baseTargetsIds;

    @Autowired
    public TwitterHelperService(TwitterConsumerRepository twitterClientRepository, TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
        this.twitterConsumerRepository = twitterClientRepository;
    }

    @PostConstruct
    public void init() {
        initTargets();
        initTwitterClients();
    }

    private void initTargets() {
        List<Target> targets = targetRepository.findAll();

        baseTargetsIds = targets.stream()
                .map(Target::getIdentifier)
                .collect(Collectors.toList());
    }

    public void initTwitterClients() {
        List<TwitterConsumer> consumers = twitterConsumerRepository.findAll();

        this.twitterRegularClients = consumers.stream()
                .map(TwitterRegularClient::new)
                .collect(Collectors.toList());
    }

    public Boolean checkBasePass(String id) {
        return baseTargetsIds.contains(id);
    }

    public Boolean checkLiveReleasePass(String id) {
        return liveReleaseTargetsIds.contains(id);
    }

    public User retrieveUser(String screenName) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        User user = null;
        params.add("screen_name", screenName);
        TwitterRegularClient currentClient = refreshClient();

        User[] userResponseArray = currentClient.getUser(params).getBody();

        if (userResponseArray != null && userResponseArray.length > 0) {
            user = userResponseArray[0];
        }

        return user;
    }

    public FollowsList getFollowsList(String id) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", id);
        params.add("stringify_ids", "true");
        TwitterRegularClient currentClient = refreshClient();

        return currentClient.getFollows(params).getBody();
    }

    public TwitterRegularClient refreshClient() {
        TwitterRegularClient client = twitterRegularClients.remove(0);
        twitterRegularClients.add(client);

        return client;
    }

    public Boolean isTweetInCache(String id) {
        if (tweetsCache.contains(id)) {
            return true;
        } else {
            tweetsCache.add(id);
            removeFromCache(id, tweetsCache);
            return false;
        }
    }

    public Boolean isTweetInV2Cache(String id) {
        if (v2TweetsCache.contains(id)) {
            return true;
        } else {
            v2TweetsCache.add(id);
            removeFromCache(id, v2TweetsCache);
            return false;
        }
    }

    public Boolean isTweetInGraphQLCache(String id) {
        if (graphQLTweetsCache.contains(id)) {
            return true;
        } else {
            graphQLTweetsCache.add(id);
            removeFromCache(id, graphQLTweetsCache);
            return false;
        }
    }

    public void removeFromCache(String id, List<String> cache) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                cache.remove(id);
            }
        };

        timer.schedule(timerTask, 60000);
    }

    public List<String> getLiveReleaseTargetsIds() {
        return liveReleaseTargetsIds;
    }

    public List<String> getLiveReleaseTargetsScreenNames() {
        return liveReleaseTargetsScreenNames;
    }

    public List<String> getBaseTargetsIds() {
        return baseTargetsIds;
    }
}
