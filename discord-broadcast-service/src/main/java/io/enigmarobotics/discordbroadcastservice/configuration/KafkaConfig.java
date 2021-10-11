package io.enigmarobotics.discordbroadcastservice.configuration;

import io.enigmasolutions.broadcastmodels.*;

import java.util.HashMap;
import java.util.Map;

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

@EnableKafka
@Configuration
public class KafkaConfig {

  @Value(value = "${kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${kafka.tweet-consumer-base.group-id}")
  private String discordBroadcastTweetGroupId;

  @Value(value = "${kafka.recognition-consumer-base.group-id}")
  private String discordBroadcastRecognitionGroupId;

  @Value(value = "${kafka.alert-consumer.group-id}")
  private String discordBroadcastAlertGroupId;

  @Bean
  public ConsumerFactory<String, Tweet> tweetConsumerFactory() {

    Map<String, Object> props = generateProps(discordBroadcastTweetGroupId);

    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), new JsonDeserializer<>(Tweet.class, false));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Tweet>
      tweetKafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, Tweet> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(tweetConsumerFactory());
    factory.setConcurrency(2);
    factory.getContainerProperties().setPollTimeout(3000);

    return factory;
  }

  @Bean
  public ConsumerFactory<String, Recognition> recognitionConsumerFactory() {

    Map<String, Object> props = generateProps(discordBroadcastRecognitionGroupId);

    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), new JsonDeserializer<>(Recognition.class, false));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Recognition>
      recognitionKafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, Recognition> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(recognitionConsumerFactory());

    return factory;
  }

  @Bean
  public ConsumerFactory<String, Alert> alertConsumerFactory() {

    Map<String, Object> props = generateProps(discordBroadcastAlertGroupId);

    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), new JsonDeserializer<>(Alert.class, false));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Alert>
      alertKafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, Alert> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(alertConsumerFactory());

    return factory;
  }

  @Bean
  public ConsumerFactory<String, UserUpdate> userUpdatesConsumerFactory() {

    Map<String, Object> props = generateProps(discordBroadcastRecognitionGroupId);

    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), new JsonDeserializer<>(UserUpdate.class, false));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UserUpdate>
      userUpdatesKafkaListenerContainerFactory() {

    ConcurrentKafkaListenerContainerFactory<String, UserUpdate> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(userUpdatesConsumerFactory());

    return factory;
  }

  private Map<String, Object> generateProps(String groupId) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    return props;
  }
}
