package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.models.external.MonitorStatus;
import io.enigmasolutions.twittermonitor.models.external.UserTimelineClusterRestoreBody;
import io.enigmasolutions.twittermonitor.services.monitoring.UserTimelineCluster;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/start/users/{screenName}")
  public void start(@PathVariable String screenName) {
    userTimelineCluster.start(screenName);
  }

  @PostMapping("/stop")
  public void stop() {
    userTimelineCluster.stop();
  }

  @PostMapping("/stop/users/{screenName}")
  public void stop(@PathVariable String screenName) {
    userTimelineCluster.stop(screenName);
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
