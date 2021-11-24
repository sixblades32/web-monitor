package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.services.monitoring.TwitterHelperService;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineCluster;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserTimelineProxyCacheScheduler {

  private final TwitterHelperService twitterHelperService;

  public UserTimelineProxyCacheScheduler(TwitterHelperService twitterHelperService) {
    this.twitterHelperService = twitterHelperService;
  }

  @Scheduled(fixedRate = 15000)
  public void scheduleCacheUpdate() {
    List<RestTemplateProxy> customerDiscordBroadcastConfigs = twitterHelperService.updateProxyPull();

    log.info(
        "Received {} UserTimelineProxy objects while updating proxies cache",
        customerDiscordBroadcastConfigs.size()
    );
  }
}
