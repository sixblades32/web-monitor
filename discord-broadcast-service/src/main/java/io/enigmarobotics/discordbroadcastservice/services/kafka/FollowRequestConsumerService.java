package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmasolutions.broadcastmodels.FollowRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class FollowRequestConsumerService {
    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

    private final PostmanService postmanService;

    @Autowired
    FollowRequestConsumerService(PostmanService postmanService) {
        this.postmanService = postmanService;
    }

    @KafkaListener(topics = "${kafka.follow-request-consumer.topic}",
            groupId = "${kafka.follow-request-consumer.group-id}",
            containerFactory = "followRequestKafkaListenerContainerFactory")
    public void consume(FollowRequest followRequest) {
        log.info("Received follow request: {}", followRequest);

        PROCESSING_EXECUTOR.execute(() -> {
            postmanService.sendFollowRequestEmbed(followRequest);
        });
    }
}
