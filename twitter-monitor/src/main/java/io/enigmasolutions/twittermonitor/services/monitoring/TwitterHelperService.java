package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.broadcastmodels.Alert;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.models.references.Proxy;
import io.enigmasolutions.twittermonitor.db.repositories.RestTemplateProxyRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.InvalidProxyNumberException;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.common.FollowingData;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.web.TwitterRegularClient;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
public class TwitterHelperService {

  private final HashMap<String, String> liveReleaseTargets = new HashMap<>();
  private final List<String> liveReleaseTargetsScreenNames = new LinkedList<>();
  private final List<String> tweetsCache = new LinkedList<>();
  private final List<User> userInfosCache = new LinkedList<>();
  private final List<String> v2TweetsCache = new LinkedList<>();
  private final List<String> graphQLTweetsCache = new LinkedList<>();

  private final TwitterConsumerRepository twitterConsumerRepository;
  private final TargetRepository targetRepository;
  private final RestTemplateProxyRepository restTemplateProxyRepository;
  private final TwitterScraperRepository twitterScraperRepository;
  private final KafkaProducer kafkaProducer;

  private List<TwitterRegularClient> helpers;
  private List<TwitterRegularClient> followers;
  private HashMap<String, String> baseTargets;

  private static final int FOLLOW_DELAY = 600000;

  @Autowired
  public TwitterHelperService(
      TwitterConsumerRepository twitterClientRepository, TwitterScraperRepository twitterScraperRepository,
      TargetRepository targetRepository,
      RestTemplateProxyRepository restTemplateProxyRepository, KafkaProducer kafkaProducer) {
    this.targetRepository = targetRepository;
    this.twitterConsumerRepository = twitterClientRepository;
    this.twitterScraperRepository = twitterScraperRepository;
    this.restTemplateProxyRepository = restTemplateProxyRepository;
    this.kafkaProducer = kafkaProducer;
  }

  @PostConstruct
  public void init() {
    initTargets();
    initHelpers();
    initFollowers();
  }

  private void initTargets() {
    List<Target> targets = targetRepository.findAll();

    baseTargets = targets.stream().collect(Collectors.toMap(Target::getIdentifier, Target::getType, (existing, replacement) -> existing, HashMap::new));
  }

  public void initHelpers() {
    List<TwitterConsumer> consumers = twitterConsumerRepository.findAll();

    this.helpers =
        consumers.stream()
            .map(consumer -> new TwitterRegularClient(consumer.getCredentials()))
            .collect(Collectors.toList());
  }

  public Boolean checkBasePass(TwitterUser user) {
    String userType = baseTargets.get(user.getId());
    Boolean isPassed = userType != null;

    if (isPassed) {
      user.setType(userType);
    }

    return isPassed;
  }

  public Boolean checkLiveReleasePass(TwitterUser user) {
    String userType = liveReleaseTargets.get(user.getId());
    Boolean isPassed = userType != null;

    if (isPassed) {
      user.setType(userType);
    }

    return isPassed;
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

  public FollowingData getFollowsList(String id) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("user_id", id);
    params.add("stringify_ids", "true");
    TwitterRegularClient currentClient = refreshClient();

    return currentClient.getFollows(params).getBody();
  }

  public TwitterRegularClient refreshClient() {
    TwitterRegularClient client = helpers.remove(0);
    helpers.add(client);

    return client;
  }

  public Boolean isUserInfoInCache(User user) {
    if (userInfosCache.stream().anyMatch(cachedUser -> cachedUser.isInfoEqual(user))) {
      return true;
    } else {
      userInfosCache.add(user);
      removeFromUserInfosCache(user);
      return false;
    }
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
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            cache.remove(id);
          }
        };

    timer.schedule(timerTask, 60000);
  }

  public void removeFromUserInfosCache(User user) {
    Timer timer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            userInfosCache.remove(user);
          }
        };

    timer.schedule(timerTask, 10000);
  }

  public void initFollowers() {
    List<TwitterScraper> twitterScrapers = twitterScraperRepository.findAll();

    this.followers = twitterScrapers.stream()
            .filter(twitterScraper -> twitterScraper.getCredentials().getConsumerKey().length() == 25)
            .map(twitterScraper -> new TwitterRegularClient(twitterScraper.getCredentials(),
                    twitterScraper.getTwitterUser().getTwitterId(),
                    twitterScraper.getProxy()))
            .collect(Collectors.toList());
  }

  public void follow(User user) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("user_id", user.getId());
    params.add("follow", "true");

    int delay = FOLLOW_DELAY / followers.size();

    followers.forEach(follower -> {
      try {
        if(!isFollows(follower.getClientId(), user.getId())){
          follower.follow(params);
          log.info(follower.getClientId() + "successfully followed to " + user.getScreenName());

          Thread.sleep(delay);
        }
      } catch (Exception exception) {
        log.error(exception.getMessage());
        kafkaProducer.sendAlertBroadcast(Alert.builder()
                .failedMonitorId(follower.getClientId())
                .reason("Error while following. Reason: " + exception.getMessage())
                .build());
      }
    });
  }

  public void updateFollowProxies(List<Proxy> proxies){

    List<TwitterScraper> scrapers = twitterScraperRepository.findAll();
    List<TwitterScraper> followScrapers = scrapers.stream().filter(twitterScraper -> twitterScraper.getCredentials().getConsumerKey().length() == 25).collect(Collectors.toList());

    if (proxies.size() != followScrapers.size()) {
      throw new InvalidProxyNumberException(followScrapers.size());
    }

    for (int i = 0; i < proxies.size(); i++) {

      TwitterScraper scraper = followScrapers.get(i);
      scraper.setProxy(proxies.get(i));

      twitterScraperRepository.save(scraper);
    }
  }

  public boolean isFollows(String clientId, String userId){
    return getFollowsList(clientId).getIds().contains(userId);
  }

  public HashMap<String, String> getLiveReleaseTargets() {
    return liveReleaseTargets;
  }

  public List<String> getLiveReleaseTargetsScreenNames() {
    return liveReleaseTargetsScreenNames;
  }

  public HashMap<String, String> getBaseTargets() {
    return baseTargets;
  }

  @Cacheable(value = "proxies")
  public List<RestTemplateProxy> getProxyPull() {
    return restTemplateProxyRepository.findAll();
  }

  @CachePut(value = "proxies")
  public List<RestTemplateProxy> updateProxyPull() {
    return getProxyPull();
  }

  public List<User> getUserInfosCache() {
    return userInfosCache;
  }
}
