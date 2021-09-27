package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.services.monitoring.V2HomeTimelineMonitor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2-home-timeline")
public class V2HomeTimelineController {

    private final V2HomeTimelineMonitor v2HomeTimelineMonitor;

    @Autowired
    public V2HomeTimelineController(V2HomeTimelineMonitor v2HomeTimelineMonitor) {
        this.v2HomeTimelineMonitor = v2HomeTimelineMonitor;
    }

    @PostMapping("/start")
    public void start() {
        v2HomeTimelineMonitor.start();
    }


    @PostMapping("/stop")
    public void stop() {
        v2HomeTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public MonitorStatus status() {
        return v2HomeTimelineMonitor.getMonitorStatus();
    }

    @PostMapping("/restore")
    public void restore(@RequestBody TwitterScraper twitterScraper) {
        v2HomeTimelineMonitor.restoreFailedClient(new TwitterCustomClient(twitterScraper));
    }
}
