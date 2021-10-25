package io.enigmasolutions.twittermonitor.services.kafka;

import io.enigmasolutions.broadcastmodels.*;
import io.enigmasolutions.twittermonitor.configuration.kafka.KafkaProuderPropertiesConfig;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

  private final KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig;
  private final KafkaTemplate<String, Tweet> tweetKafkaTemplate;
  private final KafkaTemplate<String, Recognition> recognitionKafkaTemplate;
  private final KafkaTemplate<String, UserUpdate> userUpdatesKafkaTemplate;
  private final KafkaTemplate<String, Alert> alertKafkaTemplate;
  private final KafkaTemplate<String, FollowRequest> followRequestKafkaTemplate;

  public KafkaProducer(
      KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig,
      KafkaTemplate<String, Tweet> tweetKafkaTemplate,
      KafkaTemplate<String, Recognition> recognitionKafkaTemplate,
      KafkaTemplate<String, UserUpdate> userUpdatesKafkaTemplate,
      KafkaTemplate<String, Alert> alertKafkaTemplate,
      KafkaTemplate<String, FollowRequest> followRequestKafkaTemplate) {
    this.kafkaProuderPropertiesConfig = kafkaProuderPropertiesConfig;
    this.tweetKafkaTemplate = tweetKafkaTemplate;
    this.recognitionKafkaTemplate = recognitionKafkaTemplate;
    this.userUpdatesKafkaTemplate = userUpdatesKafkaTemplate;
    this.alertKafkaTemplate = alertKafkaTemplate;
    this.followRequestKafkaTemplate = followRequestKafkaTemplate;
  }

  public void sendTweetToBaseBroadcast(Tweet tweet) {
    tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetBaseTopic(), tweet);
  }

  public void sendTweetToLiveReleaseBroadcast(Tweet tweet) {
    tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetLiveReleaseTopic(), tweet);
  }

  public void sendRecognitionToBaseBroadcast(Recognition recognition) {
    recognitionKafkaTemplate.send(
        kafkaProuderPropertiesConfig.getTweetRecognitionBaseTopic(), recognition);
  }

  public void sendRecognitionToLiveReleaseBroadcast(Recognition recognition) {
    recognitionKafkaTemplate.send(
        kafkaProuderPropertiesConfig.getTweetRecognitionLiveReleaseTopic(), recognition);
  }

  public void sendUserUpdatesToBaseBroadcast(UserUpdate userUpdate) {
    userUpdatesKafkaTemplate.send(
        kafkaProuderPropertiesConfig.getUserUpdatesBaseTopic(), userUpdate);
  }

  public void sendUserUpdatesToLiveReleaseBroadcast(UserUpdate userUpdate) {
    userUpdatesKafkaTemplate.send(
        kafkaProuderPropertiesConfig.getUserUpdatesLiveReleaseTopic(), userUpdate);
  }

  public void sendAlertBroadcast(Alert alert) {
    alertKafkaTemplate.send(kafkaProuderPropertiesConfig.getTwitterMonitorAlertTopic(), alert);
  }

  public void sendFollowRequestBroadcast(FollowRequest followRequest) {
    followRequestKafkaTemplate.send(kafkaProuderPropertiesConfig.getTwitterMonitorFollowRequestTopic(), followRequest);
  }
}
