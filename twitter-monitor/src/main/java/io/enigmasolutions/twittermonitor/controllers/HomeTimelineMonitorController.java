package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.services.monitoring.HomeTimelineTwitterMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home-timeline")
public class HomeTimelineMonitorController {

    private final HomeTimelineTwitterMonitor homeTimelineMonitor;

    @Autowired
    public HomeTimelineMonitorController(HomeTimelineTwitterMonitor homeTimelineMonitor) {
        this.homeTimelineMonitor = homeTimelineMonitor;
    }

    @PostMapping("/start")
    public void start() {
        homeTimelineMonitor.start();
    }


    @PostMapping("/stop")
    public void stop() {
        homeTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public MonitorStatus status() {
        return homeTimelineMonitor.getMonitorStatus();
    }
}
