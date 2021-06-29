package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;

import static io.enigmasolutions.webmonitor.webbroadcastservice.utils.DataWrapperUtil.wrapData;

@Slf4j
public class AbstractConsumerService<T> {

    private final Class<T> tClass;
    private final Timeline timeline;
    private final BroadcastService broadcastService;

    public AbstractConsumerService(Class<T> tClass, Timeline timeline, BroadcastService broadcastService) {
        this.tClass = tClass;
        this.timeline = timeline;
        this.broadcastService = broadcastService;
    }

    public void consume(String data, String timestamp) throws JsonProcessingException {
        String wrappedData = wrapData(timeline, timestamp, data, tClass);
        broadcastService.tryEmitNext(wrappedData);
    }
}
