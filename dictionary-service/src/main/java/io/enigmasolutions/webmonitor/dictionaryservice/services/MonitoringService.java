package io.enigmasolutions.webmonitor.dictionaryservice.services;

import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.webmonitor.dictionaryservice.db.repositories.MonitoringTargetRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    private final MonitoringTargetRepository monitoringTargetRepository;

    public MonitoringService(MonitoringTargetRepository monitoringTargetRepository) {
        this.monitoringTargetRepository = monitoringTargetRepository;
    }

    public Mono<List<DefaultMonitoringTarget>> getDefaultTargets() {
        return monitoringTargetRepository.findAll()
                .flatMap(monitoringTarget -> {
                    DefaultMonitoringTarget defaultMonitoringTarget = DefaultMonitoringTarget.builder()
                            .identifier(monitoringTarget.getIdentifier())
                            .username(monitoringTarget.getUsername())
                            .build();

                    return Mono.just(defaultMonitoringTarget);
                })
                .collect(Collectors.toList());
    }
}
