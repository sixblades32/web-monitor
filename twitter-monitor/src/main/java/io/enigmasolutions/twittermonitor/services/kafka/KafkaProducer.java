package io.enigmasolutions.twittermonitor.services.kafka;

import io.enigmasolutions.broadcastmodels.Alert;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.twittermonitor.configuration.kafka.KafkaProuderPropertiesConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer {

    private final KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig;
    private final KafkaTemplate<String, Tweet> tweetKafkaTemplate;
    private final KafkaTemplate<String, Recognition> recognitionKafkaTemplate;
    private final KafkaTemplate<String, List<TwitterUser>> userUpdatesKafkaTemplate;
    private final KafkaTemplate<String, Alert> alertKafkaTemplate;

    public KafkaProducer(
            KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig,
            KafkaTemplate<String, Tweet> tweetKafkaTemplate,
            KafkaTemplate<String, Recognition> recognitionKafkaTemplate,
            KafkaTemplate<String, List<TwitterUser>> userUpdatesKafkaTemplate,
            KafkaTemplate<String, Alert> alertKafkaTemplate
    ) {
        this.kafkaProuderPropertiesConfig = kafkaProuderPropertiesConfig;
        this.tweetKafkaTemplate = tweetKafkaTemplate;
        this.recognitionKafkaTemplate = recognitionKafkaTemplate;
        this.userUpdatesKafkaTemplate = userUpdatesKafkaTemplate;
        this.alertKafkaTemplate = alertKafkaTemplate;
    }

    public void sendTweetToBaseBroadcast(Tweet tweet) {
        tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetBaseTopic(), tweet);
    }

    public void sendTweetToLiveReleaseBroadcast(Tweet tweet) {
        tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetLiveReleaseTopic(), tweet);
    }

    public void sendRecognitionToBaseBroadcast(Recognition recognition) {
        recognitionKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetRecognitionBaseTopic(), recognition);
    }

    public void sendRecognitionToLiveReleaseBroadcast(Recognition recognition) {
        recognitionKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetRecognitionLiveReleaseTopic(), recognition);
    }

    public void sendUserUpdatesToBaseBroadcast(List<TwitterUser> userUpdates) {
        userUpdatesKafkaTemplate.send(kafkaProuderPropertiesConfig.getUserUpdatesBaseTopic(), userUpdates);
    }

    public void sendUserUpdatesToLiveReleaseBroadcast(List<TwitterUser> userUpdates) {
        userUpdatesKafkaTemplate.send(kafkaProuderPropertiesConfig.getUserUpdatesLiveReleaseTopic(), userUpdates);
    }

    public void sentAlertBroadcast(Alert alert) {
        alertKafkaTemplate.send(kafkaProuderPropertiesConfig.getTwitterMonitorAlertTopic(), alert);
    }
}
