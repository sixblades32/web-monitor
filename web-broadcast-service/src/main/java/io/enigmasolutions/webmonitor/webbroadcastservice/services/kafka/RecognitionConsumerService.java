package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline.RECOGNITION;

@Slf4j
@Service
public class RecognitionConsumerService extends AbstractConsumerService<Recognition> {

    private final static Timeline TIMELINE = RECOGNITION;

    public RecognitionConsumerService(BroadcastService broadcastService) {
        super(Recognition.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.recognition-consumer.topic}",
            groupId = "#{\"web-broadcast-\" + T(java.util.UUID).randomUUID().toString()}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            super.consume(record);
        } catch (Exception e) {
            log.error("Failed to consume message", e);
        }
    }
}
