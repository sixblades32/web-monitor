package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TweetConsumerService {

    private final BroadcastService broadcastService;

    public TweetConsumerService(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @KafkaListener(topics = "${kafka.tweet-consumer.topic}",
            groupId = "${kafka.tweet-consumer.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consume(String tweet) {
        broadcastService.tryEmitNext(tweet);
    }
}
