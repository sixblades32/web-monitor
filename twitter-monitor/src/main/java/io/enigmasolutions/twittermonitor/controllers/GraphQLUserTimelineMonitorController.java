package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.services.monitoring.GraphQLUserTimelineMonitor;
import io.enigmasolutions.twittermonitor.services.web.TwitterCustomClient;
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
    public void start(@RequestBody UserStartForm user) {
        graphQLUserTimelineMonitor.start(user.getScreenName());
    }

    @PostMapping("/stop")
    public void stop() {
        graphQLUserTimelineMonitor.stop();
    }

    @GetMapping("/status")
    public MonitorStatus status() {
        return graphQLUserTimelineMonitor.getMonitorStatus();
    }

    @PostMapping("/restore")
    public void restore(@RequestBody TwitterScraper twitterScraper) {
        graphQLUserTimelineMonitor.restoreFailedClient(new TwitterCustomClient(twitterScraper));
    }
}
