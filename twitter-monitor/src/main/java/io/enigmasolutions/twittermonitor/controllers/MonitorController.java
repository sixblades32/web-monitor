package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.services.HomeTimelineMonitor;
import io.enigmasolutions.twittermonitor.services.TwitterHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twitter-monitor")
public class MonitorController {

    private final HomeTimelineMonitor homeTimelineMonitor;
    private final TwitterHelperService twitterHelperService;

    @Autowired
    public MonitorController(HomeTimelineMonitor homeTimelineMonitor, TwitterHelperService twitterHelperService) {
        this.homeTimelineMonitor = homeTimelineMonitor;
        this.twitterHelperService = twitterHelperService;
    }

    @PostMapping("/home-timeline/start")
    public void startHomeTimelineMonitor(){
        twitterHelperService.retrieveUser("newplayaaha");
    }

    @PostMapping("/user-timeline/start")
    public void startUserTimelineMonitor(@RequestBody String username){

    }

    @PostMapping("/graphql-user-timeline/start")
    public void startGraphQLUserTimelineMonitor(@RequestBody String username){

    }

    @PostMapping("/home-timeline/stop")
    public void stopHomeTimelineMonitor(){

    }

    @PostMapping("/user-timeline/stop")
    public void stopUserTimelineMonitor(){

    }

    @PostMapping("/graphql-user-timeline/stop")
    public void stopGraphQLUserTimelineMonitor(){

    }

    @GetMapping("/status")
    public void getMonitorStatus(){

    }

}
