package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.broadcastmodels.Staff;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.STAFF;
import static org.springframework.kafka.support.KafkaHeaders.TIMESTAMP;

@Slf4j
@Service
public class StaffConsumerService extends AbstractConsumerService<Staff> {

    private final static Timeline TIMELINE = STAFF;

    public StaffConsumerService(BroadcastService broadcastService) {
        super(Staff.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.staff-consumer.topic}",
            groupId = "${kafka.staff-consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(
            @Payload String data,
            @Header(value = TIMESTAMP) String timestamp
    ) throws JsonProcessingException {
        super.consume(data, timestamp);
    }
}
