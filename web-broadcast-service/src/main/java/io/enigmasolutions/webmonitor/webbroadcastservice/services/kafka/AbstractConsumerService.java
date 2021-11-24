package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import static io.enigmasolutions.webmonitor.webbroadcastservice.utils.DataWrapperUtil.wrapBroadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.broadcast.Broadcast;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

@Slf4j
public class AbstractConsumerService<T> {

    private final Class<T> tClass;
    private final Timeline timeline;
    private final BroadcastService broadcastService;

    public AbstractConsumerService(Class<T> tClass, Timeline timeline,
            BroadcastService broadcastService) {
        this.tClass = tClass;
        this.timeline = timeline;
        this.broadcastService = broadcastService;
    }

    public void consume(ConsumerRecord<String, String> record) throws JsonProcessingException {
        Header clientIdHeader = record.headers().lastHeader("Customer-Id");
        String clientId =
                clientIdHeader != null ? new String(clientIdHeader.value(), StandardCharsets.UTF_8)
                        : null;
        Broadcast broadcast = wrapBroadcast(
                timeline,
                record.timestamp(),
                record.value(),
                clientId,
                tClass
        );
        broadcastService.tryEmitNext(broadcast);
    }
}
