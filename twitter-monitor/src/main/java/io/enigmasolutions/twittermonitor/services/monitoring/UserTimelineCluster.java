package io.enigmasolutions.twittermonitor.services.monitoring;

import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserTimelineClusterRestoreBody;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.kafka.KafkaProducer;
import io.enigmasolutions.twittermonitor.services.recognition.ImageRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.recognition.PlainTextRecognitionProcessor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import lombok.extern.slf4j.Slf4j;
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
    private final TwitterHelperService twitterHelperService;
    private final KafkaProducer kafkaProducer;
    private final List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors;
    private final List<ImageRecognitionProcessor> imageRecognitionProcessors;

    List<UserTimelineMonitor> userTimelineMonitors = new ArrayList<>();

    public UserTimelineCluster(TwitterScraperRepository twitterScraperRepository,
                               TwitterHelperService twitterHelperService,
                               KafkaProducer kafkaProducer,
                               List<PlainTextRecognitionProcessor> plainTextRecognitionProcessors,
                               List<ImageRecognitionProcessor> imageRecognitionProcessors) {
        this.twitterScraperRepository = twitterScraperRepository;
        this.twitterHelperService = twitterHelperService;
        this.kafkaProducer = kafkaProducer;
        this.plainTextRecognitionProcessors = plainTextRecognitionProcessors;
        this.imageRecognitionProcessors = imageRecognitionProcessors;
    }

    public void start(String screenName) {
        try {
            User user = twitterHelperService.retrieveUser(screenName);

            UserTimelineMonitor userTimelineMonitor = new UserTimelineMonitor(twitterScraperRepository,
                    twitterHelperService,
                    kafkaProducer,
                    plainTextRecognitionProcessors,
                    imageRecognitionProcessors, user);

            userTimelineMonitors.add(userTimelineMonitor);

            userTimelineMonitor.start();

        } catch (HttpClientErrorException e) {

            log.error(e.getMessage());

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoTwitterUserMatchesException();
            }
        }
    }

    public void stop(String screenName) {

        UserTimelineMonitor userTimelineMonitorForStop = null;

        String lowerCaseScreenName = screenName.toLowerCase(Locale.ROOT);

        userTimelineMonitorForStop = userTimelineMonitors.stream()
                .filter(userTimelineMonitor -> userTimelineMonitor.getUser()
                        .getScreenName()
                        .toLowerCase(Locale.ROOT)
                        .equals(lowerCaseScreenName))
                .findFirst()
                .orElse(null);

        if (userTimelineMonitorForStop != null) {
            userTimelineMonitorForStop.stop();
            userTimelineMonitors.remove(userTimelineMonitorForStop);
        }else{
            throw new NoTwitterUserMatchesException();
        }
    }

    public void stop(){
        userTimelineMonitors.forEach(UserTimelineMonitor::stop);
        userTimelineMonitors.clear();
    }

    public List<MonitorStatus> getMonitorStatus() {
        return userTimelineMonitors.stream().map(UserTimelineMonitor::getMonitorStatus).collect(Collectors.toList());
    }

    public void restoreFailedClient(UserTimelineClusterRestoreBody userTimelineClusterRestoreBody) {

        for (UserTimelineMonitor userTimelineMonitor : userTimelineMonitors) {
            if (userTimelineMonitor.getUser().getScreenName().equals(userTimelineClusterRestoreBody.getScreenName())) {
                userTimelineMonitor.restoreFailedClient(new TwitterCustomClient(userTimelineClusterRestoreBody.getTwitterScraper()));
            }
        }
    }
}
