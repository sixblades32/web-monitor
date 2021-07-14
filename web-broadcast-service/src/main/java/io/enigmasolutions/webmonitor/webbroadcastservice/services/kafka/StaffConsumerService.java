package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.broadcastmodels.Staff;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.STAFF;

@Slf4j
@Service
public class StaffConsumerService extends AbstractConsumerService<Staff> {

    private final static Timeline TIMELINE = STAFF;

    public StaffConsumerService(BroadcastService broadcastService) {
        super(Staff.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.staff-consumer.topic}",
            groupId = "${kafka.staff-consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) throws JsonProcessingException {
        super.consume(record);
    }
}
