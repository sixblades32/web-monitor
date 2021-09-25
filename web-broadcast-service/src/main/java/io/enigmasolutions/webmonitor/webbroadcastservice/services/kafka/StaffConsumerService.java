package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import io.enigmasolutions.broadcastmodels.Staff;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.external.Timeline.STAFF;

@Slf4j
@Service
public class StaffConsumerService extends AbstractConsumerService<Staff> {

    private final static Timeline TIMELINE = STAFF;

    public StaffConsumerService(BroadcastService broadcastService) {
        super(Staff.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.staff-consumer.topic}",
            groupId = "#{\"web-broadcast-\" + T(java.util.UUID).randomUUID().toString()}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            super.consume(record);
        } catch (Exception e) {
            log.error("Failed to consume message", e);
        }
    }
}
