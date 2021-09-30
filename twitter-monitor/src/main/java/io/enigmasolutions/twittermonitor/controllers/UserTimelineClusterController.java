package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserTimelineClusterRestoreBody;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineCluster;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-timeline")
public class UserTimelineClusterController {

  private final UserTimelineCluster userTimelineCluster;

  @Autowired
  public UserTimelineClusterController(UserTimelineCluster userTimelineCluster) {
    this.userTimelineCluster = userTimelineCluster;
  }

  @PostMapping("/start")
  public void start() {
    userTimelineCluster.start();
  }

  @PostMapping("/stop")
  public void stop() {
    userTimelineCluster.stop();
  }

  @GetMapping("/status")
  public List<MonitorStatus> status() {
    return userTimelineCluster.getMonitorStatus();
  }

  @PostMapping("/restore")
  public void restore(@RequestBody UserTimelineClusterRestoreBody userTimelineClusterRestoreBody) {
    userTimelineCluster.restoreFailedClient(userTimelineClusterRestoreBody);
  }
}
