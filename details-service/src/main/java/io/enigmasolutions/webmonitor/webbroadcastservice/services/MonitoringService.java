package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.web.DictionaryClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MonitoringService {

    private final DictionaryClient dictionaryClient;

    public MonitoringService(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }

    public Mono<List<DefaultMonitoringTarget>> getDefaultTargets() {
        return dictionaryClient.getMonitoringDefaultTargets();
    }
}
