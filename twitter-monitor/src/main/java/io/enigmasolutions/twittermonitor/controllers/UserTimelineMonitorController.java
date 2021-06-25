package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.monitor.UserStartForm;
import io.enigmasolutions.twittermonitor.services.UserTimelineMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twitter-monitor/user-timeline")
public class UserTimelineMonitorController {
    private UserTimelineMonitor userTimelineMonitor;

    @Autowired
    public UserTimelineMonitorController(UserTimelineMonitor userTimelineMonitor){
        this.userTimelineMonitor = userTimelineMonitor;
    }

    @PostMapping("/start")
    public void start(@RequestBody UserStartForm body){
        userTimelineMonitor.start(body.getUserName());
    }

    @PostMapping("/stop")
    public void stop(){
        userTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public void status(){

    }
}
