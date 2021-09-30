package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline.HEARTBEAT;
import static io.enigmasolutions.webmonitor.webbroadcastservice.utils.DataWrapperUtil.wrapBroadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.broadcast.Broadcast;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatService {

  private final BroadcastService broadcastService;

  public HeartBeatService(BroadcastService broadcastService) {
    this.broadcastService = broadcastService;
  }

  @Scheduled(fixedRate = 20000L)
  public void scheduleHeartBeat() throws JsonProcessingException {
    Broadcast broadcast = wrapBroadcast(HEARTBEAT, Instant.now().getEpochSecond(), null, null,
        null);
    broadcastService.tryEmitNext(broadcast);
  }
}
