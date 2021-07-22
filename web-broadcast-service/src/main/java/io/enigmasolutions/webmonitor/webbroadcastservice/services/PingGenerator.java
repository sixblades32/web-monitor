package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PingGenerator {

    private final BroadcastService broadcastService;

    public PingGenerator(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @Scheduled(fixedRate = 20000L)
    public void emmitPing() {
        broadcastService.tryEmitNext("ping");
    }
}
