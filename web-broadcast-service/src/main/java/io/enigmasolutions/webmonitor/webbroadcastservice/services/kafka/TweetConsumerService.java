package io.enigmasolutions.webmonitor.webbroadcastservice.services.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline.TWITTER;
import static io.enigmasolutions.webmonitor.webbroadcastservice.utils.DataWrapperUtil.wrapData;

@Slf4j
@Service
public class TweetConsumerService {

    private final static Timeline TIMELINE = TWITTER;

    private final BroadcastService broadcastService;

    public TweetConsumerService(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @KafkaListener(topics = "${kafka.tweet-consumer.topic}",
            groupId = "${kafka.tweet-consumer.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consume(String data) throws JsonProcessingException {
        String wrappedData = wrapData(TIMELINE, data, Tweet.class);
        broadcastService.tryEmitNext(wrappedData);
    }
}
