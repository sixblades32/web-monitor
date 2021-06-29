package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.monitor.Status;
import io.enigmasolutions.twittermonitor.services.monitoring.HomeTimelineTwitterMonitor;
import io.enigmasolutions.twittermonitor.services.monitoring.TwitterHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitter-monitor/home-timeline")
public class HomeTimelineMonitorController {

    private final HomeTimelineTwitterMonitor homeTimelineMonitor;

    @Autowired
    public HomeTimelineMonitorController(HomeTimelineTwitterMonitor homeTimelineMonitor, TwitterHelperService twitterHelperService) {
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
    public Status status() {
        return homeTimelineMonitor.getStatus();
    }

}
