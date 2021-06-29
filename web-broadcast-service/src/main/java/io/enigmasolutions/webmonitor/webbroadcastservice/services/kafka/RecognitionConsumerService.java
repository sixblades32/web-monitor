package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.RECOGNITION;
import static org.springframework.kafka.support.KafkaHeaders.TIMESTAMP;

@Slf4j
@Service
public class RecognitionConsumerService extends AbstractConsumerService<Recognition> {

    private final static Timeline TIMELINE = RECOGNITION;

    public RecognitionConsumerService(BroadcastService broadcastService) {
        super(Recognition.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.recognition-consumer.topic}",
            groupId = "${kafka.recognition-consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(
            @Payload String data,
            @Header(value = TIMESTAMP) String timestamp
    ) throws JsonProcessingException {
        super.consume(data, timestamp);
    }
}
