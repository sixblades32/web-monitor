package io.enigmasolutions.staffmanagerbot.services.kafka;

import io.enigmasolutions.broadcastmodels.Staff;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

  private final KafkaTemplate<String, Staff> staffMessageKafkaTemplate;

  @Value("${kafka.producer.twitter-staff-broadcast}")
  private String staffTopic;
  @Value("${kafka.producer.header}")
  private String header;

  public KafkaProducer(KafkaTemplate<String, Staff> staffMessageKafkaTemplate) {
    this.staffMessageKafkaTemplate = staffMessageKafkaTemplate;
  }

  public void sendStaffMessage(Staff staffMessage, String customerId) {
    ProducerRecord<String, Staff> producerRecord = new ProducerRecord<String, Staff>(staffTopic,
        staffMessage);
    producerRecord.headers().add(header, customerId.getBytes(StandardCharsets.UTF_8));
    staffMessageKafkaTemplate.send(producerRecord);
  }
}
