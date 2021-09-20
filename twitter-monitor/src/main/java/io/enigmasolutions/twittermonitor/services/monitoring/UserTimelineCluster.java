package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.db.repositories.RestTemplateProxyRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserTimelineClusterRestoreBody;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserTimelineCluster {

    private final TwitterScraperRepository twitterScraperRepository;
    private final RestTemplateProxyRepository restTemplateProxyRepository;
    private final TargetRepository targetRepository;
    private final TwitterHelperService twitterHelperService;
    private final KafkaProducer kafkaProducer;
    private final List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors;
    private final List<ImageRecognitionProcessor> imageRecognitionProcessors;

    List<UserTimelineMonitor> userTimelineMonitors = new ArrayList<>();
    List<RestTemplateProxy> userTimelineProxies = new ArrayList<>();
    List<RestTemplateProxy> proxiesInUse = new ArrayList<>();

    public UserTimelineCluster(TwitterScraperRepository twitterScraperRepository,
                               RestTemplateProxyRepository restTemplateProxyRepository,
                               TargetRepository targetRepository,
                               TwitterHelperService twitterHelperService,
                               KafkaProducer kafkaProducer,
                               List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
                               List<ImageRecognitionProcessor> imageRecognitionProcessors) {
        this.twitterScraperRepository = twitterScraperRepository;
        this.restTemplateProxyRepository = restTemplateProxyRepository;
        this.targetRepository = targetRepository;
        this.twitterHelperService = twitterHelperService;
        this.kafkaProducer = kafkaProducer;
        this.plainTextRecognitionProcessors = plainTextRecognitionProcessors;
        this.imageRecognitionProcessors = imageRecognitionProcessors;
    }

    public void start() {
        try {
            userTimelineProxies = getProxyPull();
            List<Target> targets = getTargets();

            if (userTimelineProxies.size() < targets.size()) {
                log.info("Only {} out of {} targets will be loaded, cause not enough number of proxies", userTimelineProxies.size(), targets.size());
                targets = targets.subList(0, userTimelineProxies.size());
            }

            targets.forEach(target -> {
                User user = new User();
                user.setScreenName(target.getUsername());
                user.setId(target.getIdentifier());

                RestTemplateProxy proxy = getAvailableProxy();

                UserTimelineMonitor userTimelineMonitor = new UserTimelineMonitor(twitterScraperRepository,
                        twitterHelperService,
                        kafkaProducer,
                        plainTextRecognitionProcessors,
                        imageRecognitionProcessors, user, proxy);

                userTimelineMonitors.add(userTimelineMonitor);

                userTimelineMonitor.start();
            });


        } catch (HttpClientErrorException e) {

            log.error(e.getMessage());

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoTwitterUserMatchesException();
            }
        }
    }

    public void stop() {
        userTimelineMonitors.forEach(UserTimelineMonitor::stop);
        proxiesInUse.clear();
        userTimelineMonitors.clear();
    }

    public RestTemplateProxy getAvailableProxy() {
        RestTemplateProxy availableProxy = null;
        if (!userTimelineProxies.isEmpty()) {
            List<RestTemplateProxy> availableProxies = userTimelineProxies
                    .stream()
                    .filter(proxy -> !proxiesInUse.contains(proxy))
                    .collect(Collectors.toList());
            if (!availableProxies.isEmpty()) {
                availableProxy = availableProxies.get(0);
                proxiesInUse.add(availableProxy);
            }
        }

        return availableProxy;
    }

    public List<MonitorStatus> getMonitorStatus() {
        return userTimelineMonitors.stream().map(UserTimelineMonitor::getMonitorStatus).collect(Collectors.toList());
    }

    public List<Target> getTargets() {
        return targetRepository.findAll();
    }

    @Cacheable(value = "proxies")
    public List<RestTemplateProxy> getProxyPull() {
        return restTemplateProxyRepository.findAll();
    }

    @CachePut(value = "proxies")
    public List<RestTemplateProxy> updateProxyPull() {
        return getProxyPull();
    }

    public void restoreFailedClient(UserTimelineClusterRestoreBody userTimelineClusterRestoreBody) {

        for (UserTimelineMonitor userTimelineMonitor : userTimelineMonitors) {
            if (userTimelineMonitor.getUser().getScreenName().equals(userTimelineClusterRestoreBody.getScreenName())) {
                userTimelineMonitor.restoreFailedClient(new TwitterCustomClient(userTimelineClusterRestoreBody.getTwitterScraper()));
            }
        }
    }
}
