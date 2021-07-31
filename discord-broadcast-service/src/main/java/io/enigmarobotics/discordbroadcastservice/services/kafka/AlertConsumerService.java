package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class AlertConsumerService {

    private final PostmanService postmanService;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;

    @Autowired
    AlertConsumerService(PostmanService postmanService, DiscordEmbedColorConfig discordEmbedColorConfig) {
        this.postmanService = postmanService;
        this.discordEmbedColorConfig = discordEmbedColorConfig;
    }

    @KafkaListener(topics = "${kafka.alert-consumer.topic}",
            groupId = "${kafka.alert-consumer.group-id}",
            containerFactory = "alertKafkaListenerContainerFactory")
    public void consume(Alert alert) {

        log.info("Received Alert Message");

        Embed alertEmbed = DiscordUtils.generateAlertEmbed(alert, discordEmbedColorConfig.getAlert());

        Message message = Message.builder()
                .content("")
                .embeds(Collections.singletonList(alertEmbed))
                .build();

        postmanService.sendAlertEmbed(message);
    }
}
