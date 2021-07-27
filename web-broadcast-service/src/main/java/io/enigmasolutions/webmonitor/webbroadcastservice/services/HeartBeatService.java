package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.HEARTBEAT;
import static io.enigmasolutions.webmonitor.webbroadcastservice.utils.DataWrapperUtil.wrapData;

@Service
public class HeartBeatService {

    private final BroadcastService broadcastService;

    public HeartBeatService(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @Scheduled(fixedRate = 20000L)
    public void scheduleHeartBeat() throws JsonProcessingException {
        String wrappedData = wrapData(HEARTBEAT, Instant.now().getEpochSecond(), null, null);
        broadcastService.tryEmitNext(wrappedData);
    }
}
