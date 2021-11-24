package io.enigmasolutions.webmonitor.dictionaryservice.controllers;

import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents.MonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.services.MonitoringService;
import java.util.List;

import org.springframework.web.bind.annotation.*;
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

  @PostMapping("/targets")
  public Mono<MonitoringTarget> createMonitoringTarget(@RequestBody DefaultMonitoringTarget defaultMonitoringTarget){
    return monitoringService.createMonitoringTarget(defaultMonitoringTarget);
  }

  @DeleteMapping("/targets/{id}")
  public Mono<MonitoringTarget> deleteMonitoringTarget(@PathVariable String id){
    return monitoringService.deleteMonitoringTarget(id);
  }
}
