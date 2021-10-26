package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.broadcastmodels.UserUpdate;
import io.enigmasolutions.broadcastmodels.UserUpdateType;
import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import io.enigmasolutions.twittermonitor.utils.TweetGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public class UserTimelineMonitor extends AbstractTwitterMonitor {

  private static final String TIMELINE_PATH = "statuses/user_timeline.json";
  private final RestTemplateProxy proxy;
  private User user;

  public UserTimelineMonitor(
      TwitterScraperRepository twitterScraperRepository,
      TwitterHelperService twitterHelperService,
      KafkaProducer kafkaProducer,
      List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
      List<ImageRecognitionProcessor> imageRecognitionProcessors,
      User user,
      RestTemplateProxy proxy) {
    super(
        700,
        twitterScraperRepository,
        twitterHelperService,
        kafkaProducer,
        plainTextRecognitionProcessors,
        imageRecognitionProcessors,
        log);

    this.user = user;
    this.proxy = proxy;
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
    return MonitorStatus.builder().status(super.getStatus()).user(user).build();
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
  protected void processTweetResponse(TweetResponse tweetResponse) {
    super.processingExecutor.execute(() -> super.processTweetResponse(tweetResponse));
    super.processingExecutor.execute(() -> processUser(tweetResponse));
  }

  private void processUser(TweetResponse tweetResponse) {
    if (tweetResponse == null) {
      return;
    }

    User currentUser = tweetResponse.getUser();

    if (user.getDescription() == null) {
      user = currentUser;
    }

    if (!user.isInfoEqual(currentUser)) {

      if (twitterHelperService.isUserInfoInCache(currentUser)) {
        return;
      }

      twitterHelperService.isUserInfoInCache(user);
      user.setUpdateTypes(currentUser);
      TwitterUser twitterUser = TweetGenerator.buildTweetUser(user);
      List<UserUpdateType> userUpdateTypes = user.getUpdateTypes();

      user = currentUser;
      TwitterUser updatedTwitterUser = TweetGenerator.buildTweetUser(user);

      UserUpdate userUpdate =
          UserUpdate.builder()
              .updateTypes(userUpdateTypes)
              .old(twitterUser)
              .updated(updatedTwitterUser)
              .build();

      super.processingExecutor.execute(() -> processCommonTargetUpdates(userUpdate));
      super.processingExecutor.execute(() -> processLiveReleaseTargetUpdates(userUpdate));
    }
  }

  private void processCommonTargetUpdates(UserUpdate userUpdate) {
    if (super.isCommonTargetValid(userUpdate.getOld())) {
      super.kafkaProducer.sendUserUpdatesToBaseBroadcast(userUpdate);
    }
  }

  private void processLiveReleaseTargetUpdates(UserUpdate userUpdate) {
    if (super.isLiveReleaseTargetValid(userUpdate.getOld())) {
      super.kafkaProducer.sendUserUpdatesToLiveReleaseBroadcast(userUpdate);
    }
  }

  protected TweetResponse getTweetResponse(
      MultiValueMap<String, String> params,
      String timelinePath,
      TwitterCustomClient twitterCustomClient) {
    TweetResponse tweetResponse = null;

    TweetResponse[] tweetResponseArray =
        twitterCustomClient.getProxiedBaseApiTimelineTweets(params, timelinePath).getBody();

    if (tweetResponseArray != null && tweetResponseArray.length > 0) {
      tweetResponse = tweetResponseArray[0];
    }

    return tweetResponse;
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
    this.twitterCustomClients =
        scrapers.stream()
            .map(scraper -> new TwitterCustomClient(scraper, proxy))
            .collect(Collectors.toList());

    twitterCustomClients = Collections.synchronizedList(twitterCustomClients);
  }

  public User getUser() {
    return user;
  }

  public RestTemplateProxy getProxy() {
    return proxy;
  }
}
