package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.models.external.UserTimelineClusterRestoreBody;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-timeline")
public class UserTimelineClusterController {

    private final UserTimelineCluster userTimelineCluster;

    @Autowired
    public UserTimelineClusterController(UserTimelineCluster userTimelineCluster) {
        this.userTimelineCluster = userTimelineCluster;
    }

    @PostMapping("/start")
    public void start(@RequestBody UserStartForm user) {
        userTimelineCluster.start(user.getScreenName());
    }

    @PostMapping("/stop")
    public void stop(@RequestBody UserStartForm user) {
        userTimelineCluster.stop(user.getScreenName());
    }

    @GetMapping("/status")
    public List<MonitorStatus> status() {
        return userTimelineCluster.getMonitorStatus();
    }

    //
    @PostMapping("/restore")
    public void restore(@RequestBody UserTimelineClusterRestoreBody userTimelineClusterRestoreBody) {
        userTimelineCluster.restoreFailedClient(userTimelineClusterRestoreBody);
    }
}
