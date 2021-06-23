package io.enigmarobotics.discordbroadcastservice.configuration;

import io.enigmarobotics.discordbroadcastservice.domain.wrappers.Alert;
import io.enigmasolutions.broadcastmodels.Tweet;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.tweet-consumer.group-id}")
    private String discordBroadcastTweetGroupId;

    @Value(value = "${kafka.alert-consumer.group-id}")
    private String discordBroadcastAlertGroupId;

    @Bean
    public ConsumerFactory<String, Tweet> tweetConsumerFactory (){

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, discordBroadcastTweetGroupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(Tweet.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Tweet>
    tweetKafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, Tweet> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tweetConsumerFactory());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, Alert> alertConsumerFactory (){

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, discordBroadcastAlertGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(Alert.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Alert>
    alertKafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, Alert> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(alertConsumerFactory());

        return factory;
    }
}
