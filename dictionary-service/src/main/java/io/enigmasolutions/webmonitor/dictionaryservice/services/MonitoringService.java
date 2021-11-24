package io.enigmasolutions.webmonitor.dictionaryservice.services;

import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents.MonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.db.repositories.MonitoringTargetRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MonitoringService {

  private final MonitoringTargetRepository monitoringTargetRepository;

  public MonitoringService(MonitoringTargetRepository monitoringTargetRepository) {
    this.monitoringTargetRepository = monitoringTargetRepository;
  }

  public Mono<List<DefaultMonitoringTarget>> getDefaultTargets() {
    return monitoringTargetRepository
        .findAll()
        .flatMap(
            monitoringTarget -> {
              DefaultMonitoringTarget defaultMonitoringTarget =
                  DefaultMonitoringTarget.builder()
                      .identifier(monitoringTarget.getIdentifier())
                      .username(monitoringTarget.getUsername())
                      .image(monitoringTarget.getImage())
                      .name(monitoringTarget.getName())
                      .build();

              return Mono.just(defaultMonitoringTarget);
            })
        .collect(Collectors.toList());
  }

  public Mono<MonitoringTarget> createMonitoringTarget(
      DefaultMonitoringTarget defaultMonitoringTarget) {

    return monitoringTargetRepository.save(
        MonitoringTarget.builder()
            .username(defaultMonitoringTarget.getUsername())
            .name(defaultMonitoringTarget.getName())
            .identifier(defaultMonitoringTarget.getIdentifier())
            .image(defaultMonitoringTarget.getImage())
            .build());
  }

  public Mono<MonitoringTarget> deleteMonitoringTarget(String userId) {
    return monitoringTargetRepository.deleteTargetByIdentifier(userId);
  }
}
