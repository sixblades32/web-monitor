package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.services.GraphQLUserTimelineMonitor;
import io.enigmasolutions.twittermonitor.services.UserTimelineMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twitter-monitor/graphql-user-timeline")
public class GraphQLUserTimelineMonitorController {
    private GraphQLUserTimelineMonitor graphQLUserTimelineMonitor;

    @Autowired
    public GraphQLUserTimelineMonitorController(GraphQLUserTimelineMonitor graphQLUserTimelineMonitor){
        this.graphQLUserTimelineMonitor = graphQLUserTimelineMonitor;
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
