package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineCluster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserTimelineProxyCacheScheduler {
    private final UserTimelineCluster userTimelineCluster;

    public UserTimelineProxyCacheScheduler(UserTimelineCluster userTimelineCluster) {
        this.userTimelineCluster = userTimelineCluster;
    }

    @Scheduled(fixedRate = 15000)
    public void scheduleCacheUpdate() {
        List<RestTemplateProxy> customerDiscordBroadcastConfigs = userTimelineCluster.updateProxyPull();

        log.info(
                "Received {} UserTimelineProxy objects while updating proxies cache",
                customerDiscordBroadcastConfigs.size()
        );
    }
}
