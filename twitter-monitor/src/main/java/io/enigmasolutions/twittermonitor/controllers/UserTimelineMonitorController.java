package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineMonitor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-timeline")
public class UserTimelineMonitorController {

    private final UserTimelineMonitor userTimelineMonitor;

    @Autowired
    public UserTimelineMonitorController(UserTimelineMonitor userTimelineMonitor) {
        this.userTimelineMonitor = userTimelineMonitor;
    }

    @PostMapping("/start")
    public void start(@RequestBody UserStartForm user) {
        userTimelineMonitor.start(user.getScreenName());
    }

    @PostMapping("/stop")
    public void stop() {
        userTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public MonitorStatus status() {
        return userTimelineMonitor.getMonitorStatus();
    }

    @PostMapping("/restore")
    public void restore(@RequestBody TwitterScraper twitterScraper) {
        userTimelineMonitor.restoreFailedClient(new TwitterCustomClient(twitterScraper));
    }
}
