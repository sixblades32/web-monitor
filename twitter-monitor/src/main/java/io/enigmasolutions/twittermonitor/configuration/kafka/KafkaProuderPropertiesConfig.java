package io.enigmasolutions.twittermonitor.configuration.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProuderPropertiesConfig {

  private String tweetBaseTopic;
  private String tweetLiveReleaseTopic;
  private String tweetRecognitionBaseTopic;
  private String tweetRecognitionLiveReleaseTopic;
  private String userUpdatesBaseTopic;
  private String userUpdatesLiveReleaseTopic;
  private String twitterMonitorAlertTopic;
}
