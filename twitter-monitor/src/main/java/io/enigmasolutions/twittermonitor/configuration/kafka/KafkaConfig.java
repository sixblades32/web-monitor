package io.enigmasolutions.twittermonitor.configuration.kafka;

import io.enigmasolutions.broadcastmodels.Alert;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, Tweet> tweetKafkaTemplate() {
        KafkaTemplate<String, Tweet> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(new LoggingProducerListener<>());

        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Recognition> recognitionKafkaTemplate() {
        KafkaTemplate<String, Recognition> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(new LoggingProducerListener<>());

        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, Alert> alertKafkaTemplate(){
        KafkaTemplate<String, Alert> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(new LoggingProducerListener<>());

        return kafkaTemplate;
    }
}
