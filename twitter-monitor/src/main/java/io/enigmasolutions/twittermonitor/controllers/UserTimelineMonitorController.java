package io.enigmasolutions.twittermonitor.controllers;

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
    public void start(@RequestBody String username){

    }

    @PostMapping("/stop")
    public void stop(){

    }

    @GetMapping("/status")
    public void status(){

    }
}
