package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.services.monitoring.GraphQLUserTimelineMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graphql-user-timeline")
public class GraphQLUserTimelineMonitorController {

    private final GraphQLUserTimelineMonitor graphQLUserTimelineMonitor;

    @Autowired
    public GraphQLUserTimelineMonitorController(GraphQLUserTimelineMonitor graphQLUserTimelineMonitor) {
        this.graphQLUserTimelineMonitor = graphQLUserTimelineMonitor;
    }

    @PostMapping("/start")
    public void start(@RequestBody UserStartForm body) {
        graphQLUserTimelineMonitor.start(body.getUsername());
    }

    @PostMapping("/stop")
    public void stop(){
        graphQLUserTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public void status(){

    }
}
