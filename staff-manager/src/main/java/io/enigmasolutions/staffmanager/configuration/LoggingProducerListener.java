package io.enigmasolutions.staffmanager.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

@Slf4j
public class LoggingProducerListener<K, V> implements ProducerListener<K, V> {
    @Override
    public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        ProducerListener.super.onSuccess(producerRecord, recordMetadata);
        log.info("Message has been send to topic: {}, message: {}", producerRecord.topic(), producerRecord.value());
    }

    @Override
    public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
        log.error(
                "Message send error has been occurred to topic: {}, message: {}",
                producerRecord.topic(),
                producerRecord.value()
        );
    }
}
