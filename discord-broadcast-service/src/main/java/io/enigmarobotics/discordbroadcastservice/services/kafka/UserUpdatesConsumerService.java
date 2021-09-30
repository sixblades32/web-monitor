package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserUpdatesConsumerService {

  private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

  private final PostmanService postmanService;
  private final DiscordEmbedColorConfig discordEmbedColorConfig;

  @Autowired
  UserUpdatesConsumerService(PostmanService postmanService,
      DiscordEmbedColorConfig discordEmbedColorConfig) {
    this.postmanService = postmanService;
    this.discordEmbedColorConfig = discordEmbedColorConfig;
  }

  @KafkaListener(topics = "${kafka.user-updates-consumer-base.topic}",
      groupId = "${kafka.user-updates-consumer-base.group-id}",
      containerFactory = "alertKafkaListenerContainerFactory")
  public void consumeBase(List<TwitterUser> userUpdates) {
    log.info("Received base user info updates: {}", userUpdates);

    Embed userInfoUpdatesEmbed = null;
    Message message = Message.builder()
        .content("")
        .embeds(Collections.singletonList(userInfoUpdatesEmbed))
        .build();

    PROCESSING_EXECUTOR.execute(() -> {
      postmanService.processBase(message);
    });

    PROCESSING_EXECUTOR.execute(() -> {
      postmanService.processStaffBase(message);
    });
  }

  @KafkaListener(topics = "${kafka.user-updates-consumer-live-release.topic}",
      groupId = "${kafka.user-updates-consumer-live-release.group-id}",
      containerFactory = "alertKafkaListenerContainerFactory")
  public void consumeLiveRelease(List<TwitterUser> userUpdates) {
    log.info("Received live release user info updates: {}", userUpdates);

    Embed userInfoUpdatesEmbed = null;
    Message message = Message.builder()
        .content("")
        .embeds(Collections.singletonList(userInfoUpdatesEmbed))
        .build();

    PROCESSING_EXECUTOR.execute(() -> {
      postmanService.processLive(message);
    });
  }
}
