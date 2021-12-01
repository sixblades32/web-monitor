package io.enigmasolutions.twittermonitor.services.configuration;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.models.references.Proxy;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTargetMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.TargetAlreadyAddedException;
import io.enigmasolutions.twittermonitor.exceptions.TargetIsPrivateException;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.monitoring.TwitterHelperService;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.enigmasolutions.twittermonitor.services.web.DictionaryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MonitorConfigurationService {

  private final TwitterHelperService twitterHelperService;
  private final TwitterConsumerRepository twitterConsumerRepository;
  private final TwitterScraperRepository twitterScraperRepository;
  private final TargetRepository targetRepository;
  private final DictionaryClient dictionaryClient;
  private final KafkaProducer kafkaProducer;

  @Autowired
  public MonitorConfigurationService(
      TwitterHelperService twitterHelperService,
      TwitterConsumerRepository twitterConsumerRepository,
      TwitterScraperRepository twitterScraperRepository,
      TargetRepository targetRepository,
      DictionaryClient dictionaryClient,
      KafkaProducer kafkaProducer) {

    this.twitterHelperService = twitterHelperService;
    this.twitterConsumerRepository = twitterConsumerRepository;
    this.twitterScraperRepository = twitterScraperRepository;
    this.targetRepository = targetRepository;
    this.dictionaryClient = dictionaryClient;
    this.kafkaProducer = kafkaProducer;
  }

  public void createConsumer(TwitterConsumer consumer) {

    twitterConsumerRepository.insert(consumer);
  }

  public List<TwitterConsumer> getConsumers() {

    return twitterConsumerRepository.findAll();
  }

  public void deleteConsumer(TwitterConsumer twitterConsumer) {

    String id = twitterConsumer.getId();

    twitterConsumerRepository.deleteById(id);
  }

  public void createScraper(TwitterScraper scraper) {

    twitterScraperRepository.insert(scraper);
  }

  public List<TwitterScraper> getScrapers() {
    return twitterScraperRepository.findAll();
  }

  public void deleteScraper(TwitterScraper twitterScraper) {

    String id = twitterScraper.getId();

    twitterConsumerRepository.deleteById(id);
  }

  public List<Target> getGlobalTargets() {

    return targetRepository.findAll();
  }

  @Transactional
  public void createGlobalTarget(UserStartForm body) {
    User user;

    try {
      user = twitterHelperService.retrieveUser(body.getScreenName());
    } catch (Exception e) {
      throw new NoTwitterUserMatchesException();
    }

    if (twitterHelperService.getBaseTargets().get(user.getId()) != null) {
      throw new TargetAlreadyAddedException();
    }

    Target target =
        Target.builder()
            .username(user.getScreenName().toLowerCase())
            .identifier(user.getId())
            .type(body.getType())
            .build();

    DefaultMonitoringTarget defaultMonitoringTarget =
        DefaultMonitoringTarget.builder()
            .username(user.getScreenName())
            .identifier(user.getId())
            .image(user.getUserImage())
            .name(user.getName())
            .type(body.getType())
            .build();

    targetRepository.insert(target);
    dictionaryClient.createMonitoringTarget(defaultMonitoringTarget);

    twitterHelperService.getBaseTargets().put(user.getId(), body.getType());
  }

  @Transactional
  public void deleteGlobalTarget(UserStartForm body) {
    User user;

    try {
      user = twitterHelperService.retrieveUser(body.getScreenName());
    } catch (Exception e) {
      throw new NoTwitterUserMatchesException();
    }

    if (twitterHelperService.getBaseTargets().get(user.getId()) == null) {
      throw new NoTargetMatchesException();
    }

    targetRepository.deleteTargetByIdentifier(user.getId());
    dictionaryClient.deleteMonitoringTarget(user.getId());

    twitterHelperService.getBaseTargets().remove(user.getId());
  }

  public List<String> getTemporaryTargets() {

    return twitterHelperService.getLiveReleaseTargetsScreenNames();
  }

  public void createTemporaryTarget(UserStartForm body) {
    User user;

    try {
      user = twitterHelperService.retrieveUser(body.getScreenName());
    } catch (Exception e) {
      throw new NoTwitterUserMatchesException();
    }

    if (twitterHelperService.getLiveReleaseTargets().get(user.getId()) != null) {
      throw new TargetAlreadyAddedException();
    }

    twitterHelperService.getLiveReleaseTargets().put(user.getId(), body.getType());
    twitterHelperService.getLiveReleaseTargetsScreenNames().add(user.getScreenName());
  }

  public void deleteTemporaryTarget(UserStartForm body) {
    User user;

    try {
      user = twitterHelperService.retrieveUser(body.getScreenName());
    } catch (Exception e) {
      throw new NoTwitterUserMatchesException();
    }

    if (twitterHelperService.getLiveReleaseTargets().get(user.getId()) == null) {
      throw new NoTargetMatchesException();
    }

    twitterHelperService.getLiveReleaseTargets().remove(user.getId());
    twitterHelperService.getLiveReleaseTargetsScreenNames().remove(user.getScreenName());
  }

  public void createFollowRequest(FollowRequest followRequest) {

    User user = null;

    try {
      user = twitterHelperService.retrieveUser(followRequest.getTwitterUser().getLogin());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new NoTwitterUserMatchesException();
    }

    if (twitterHelperService.getBaseTargets().get(user.getId()) != null) {
      throw new TargetAlreadyAddedException();
    }

    TwitterUser twitterUser =
        TwitterUser.builder()
            .id(user.getId())
            .login(user.getScreenName())
            .name(user.getName())
            .url(user.getUserUrl())
            .build();

    followRequest.setTwitterUser(twitterUser);

    kafkaProducer.sendFollowRequestBroadcast(followRequest);
  }

  public void follow(String screenName) {
    User user = null;

    try {
      user = twitterHelperService.retrieveUser(screenName);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new NoTwitterUserMatchesException();
    }

    if (user.getIsProtected()) {
      throw new TargetIsPrivateException();
    }

    User finalUser = user;

    CompletableFuture.runAsync(
        () -> {
          twitterHelperService.follow(finalUser);
        });
  }

  public void updateFollowProxies(List<Proxy> proxies) {
    twitterHelperService.updateFollowProxies(proxies);
  }
}
