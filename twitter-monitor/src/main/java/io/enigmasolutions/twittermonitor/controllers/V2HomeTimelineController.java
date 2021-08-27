package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.services.monitoring.V2HomeTimelineMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
