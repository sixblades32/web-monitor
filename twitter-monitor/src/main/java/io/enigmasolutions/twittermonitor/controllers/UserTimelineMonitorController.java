package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineMonitor;
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
    public void start(@RequestBody UserStartForm body) {
        userTimelineMonitor.start(body.getUsername());
    }

    @PostMapping("/stop")
    public void stop(){
        userTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public void status(){

    }
}
