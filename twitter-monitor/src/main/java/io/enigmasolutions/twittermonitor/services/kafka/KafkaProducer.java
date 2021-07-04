package io.enigmasolutions.twittermonitor.services.kafka;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.configuration.kafka.KafkaProuderPropertiesConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig;
    private final KafkaTemplate<String, Tweet> tweetKafkaTemplate;
    private final KafkaTemplate<String, Recognition> recognitionKafkaTemplate;

    public KafkaProducer(
            KafkaProuderPropertiesConfig kafkaProuderPropertiesConfig, KafkaTemplate<String, Tweet> tweetKafkaTemplate,
            KafkaTemplate<String, Recognition> recognitionKafkaTemplate
    ) {
        this.kafkaProuderPropertiesConfig = kafkaProuderPropertiesConfig;
        this.tweetKafkaTemplate = tweetKafkaTemplate;
        this.recognitionKafkaTemplate = recognitionKafkaTemplate;
    }

    public void sendTweetToBaseBroadcast(Tweet tweet) {
        tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetBaseTopic(), tweet);
    }

    public void sendTweetLiveReleaseBroadcast(Tweet tweet) {
        tweetKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetLiveReleaseTopic(), tweet);
    }

    public void sendTweetToBaseBroadcastRecognition(Recognition recognition) {
        recognitionKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetRecognitionTopic(), recognition);
    }

    public void sendTweetLiveReleaseRecognition(Recognition recognition) {
        recognitionKafkaTemplate.send(kafkaProuderPropertiesConfig.getTweetRecognitionTopic(), recognition);
    }
}
