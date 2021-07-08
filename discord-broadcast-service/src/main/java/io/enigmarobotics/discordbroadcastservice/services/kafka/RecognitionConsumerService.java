package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.BroadcastRecognition;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecognitionConsumerService {

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
    public void consumeBaseTopic(BroadcastRecognition recognition) {

        log.info("Received Base Recognition Message from: " + recognition.getUserName());

        Message message = generateRecognitionMessage(recognition);

        postmanService.processCommon(message);
    }

    @KafkaListener(topics = "${kafka.recognition-consumer-live-release.topic}",
            groupId = "${kafka.recognition-consumer-live-release.group-id}",
            containerFactory = "recognitionKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(BroadcastRecognition recognition) {

        log.info("Received Live Release Recognition Message from: " + recognition.getUserName());

        Message message = generateRecognitionMessage(recognition);

        postmanService.processAdvanced(message);
    }

    private Message generateRecognitionMessage(BroadcastRecognition recognition) {

        List<Embed> embeds = recognition.getTweetType().generateRecognitionEmbed(recognition, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }
}
