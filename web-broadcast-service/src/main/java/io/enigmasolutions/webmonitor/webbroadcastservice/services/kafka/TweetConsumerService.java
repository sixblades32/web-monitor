package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.TWITTER;

@Slf4j
@Service
public class TweetConsumerService extends AbstractConsumerService<Tweet> {

    private final static Timeline TIMELINE = TWITTER;

    public TweetConsumerService(BroadcastService broadcastService) {
        super(Tweet.class, TIMELINE, broadcastService);
    }

    @Override
    @KafkaListener(topics = "${kafka.tweet-consumer.topic}",
            groupId = "${kafka.tweet-consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) throws JsonProcessingException {
        super.consume(record);
    }
}
