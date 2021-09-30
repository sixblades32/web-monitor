package io.enigmasolutions.webmonitor.dictionaryservice.controllers;

import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.services.MonitoringService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {

  private final MonitoringService monitoringService;

  public MonitoringController(MonitoringService monitoringService) {
    this.monitoringService = monitoringService;
  }

  @GetMapping("/targets/default")
  public Mono<List<DefaultMonitoringTarget>> getDefaultTargets() {
    return monitoringService.getDefaultTargets();
  }
}
