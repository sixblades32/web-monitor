package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastTweetType;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.Recognition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class RecognitionConsumerService {

    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

    private final PostmanService postmanService;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;

    @Autowired
    RecognitionConsumerService(PostmanService postmanService, DiscordEmbedColorConfig discordEmbedColorConfig) {
        this.postmanService = postmanService;
        this.discordEmbedColorConfig = discordEmbedColorConfig;
    }

    @KafkaListener(topics = "${kafka.recognition-consumer-base.topic}",
            groupId = "${kafka.recognition-consumer-base.group-id}",
            containerFactory = "recognitionKafkaListenerContainerFactory")
    public void consumeBaseTopic(Recognition recognition) {
        log.info("Received base recognition message {}", recognition);

        PROCESSING_EXECUTOR.execute(() -> {
            Message message = generateRecognitionMessage(recognition);
            postmanService.processBase(message);
        });
    }

    @KafkaListener(topics = "${kafka.recognition-consumer-live-release.topic}",
            groupId = "${kafka.recognition-consumer-live-release.group-id}",
            containerFactory = "recognitionKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(Recognition recognition) {
        log.info("Received live release recognition message {}", recognition);

        PROCESSING_EXECUTOR.execute(() -> {
            Message message = generateRecognitionMessage(recognition);
            postmanService.processLive(message);
        });
    }

    private Message generateRecognitionMessage(Recognition recognition) {

        DiscordBroadcastTweetType tweetType = DiscordUtils.convertTweetType(recognition.getTweetType());

        List<Embed> embeds = tweetType.generateRecognitionEmbed(recognition, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }
}
