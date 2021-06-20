package io.enigmasolutions.twittermonitor.configuration;

import io.enigmasolutions.twittermonitor.models.broadcast.BroadcastTweet;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
public class TweetProducerConfiguration {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.tweet-consumer.group-id}")
    private String discordBroadcastTweetGroupId;

    // TODO: remove this
    @Value(value = "${kafka.tweet-image-consumer.group-id}")
    private String discordBroadcastTweetImageGroupId;

    @Value(value = "${kafka.alert-consumer.group-id}")
    private String discordBroadcastAlertGroupId;

    @Bean
    public ProducerFactory<String, BroadcastTweet> tweetProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                discordBroadcastTweetGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, BroadcastTweet> kafkaTemplate() {
        return new KafkaTemplate<>(tweetProducerFactory());
    }
}
