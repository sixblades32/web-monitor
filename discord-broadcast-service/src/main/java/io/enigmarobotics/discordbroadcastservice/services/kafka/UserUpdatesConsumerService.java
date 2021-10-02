package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastUserUpdateType;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.enigmasolutions.broadcastmodels.UserUpdateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserUpdatesConsumerService {

  private static final ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

  private final PostmanService postmanService;
  private final DiscordEmbedColorConfig discordEmbedColorConfig;

  @Autowired
  UserUpdatesConsumerService(
      PostmanService postmanService, DiscordEmbedColorConfig discordEmbedColorConfig) {
    this.postmanService = postmanService;
    this.discordEmbedColorConfig = discordEmbedColorConfig;
  }

  @KafkaListener(
      topics = "${kafka.user-updates-consumer-base.topic}",
      groupId = "${kafka.user-updates-consumer-base.group-id}",
      containerFactory = "userUpdatesKafkaListenerContainerFactory")
  public void consumeBase(List<TwitterUser> userUpdates) {
    log.info("Received base user info updates: {}", userUpdates);

    Message message = generateUserUpdateMessage(userUpdates);

    PROCESSING_EXECUTOR.execute(
        () -> {
          postmanService.processBase(message);
        });

    PROCESSING_EXECUTOR.execute(
        () -> {
          postmanService.processStaffBase(message);
        });
  }

  @KafkaListener(
      topics = "${kafka.user-updates-consumer-live-release.topic}",
      groupId = "${kafka.user-updates-consumer-live-release.group-id}",
      containerFactory = "userUpdatesKafkaListenerContainerFactory")
  public void consumeLiveRelease(List<TwitterUser> userUpdates) {
    log.info("Received live release user info updates: {}", userUpdates);

    Message message = generateUserUpdateMessage(userUpdates);

    PROCESSING_EXECUTOR.execute(
        () -> {
          postmanService.processLive(message);
        });
  }

  private Message generateUserUpdateMessage(List<TwitterUser> userUpdates) {
    List<Embed> embeds = new LinkedList<>();

    for (UserUpdateType userUpdateType : userUpdates.get(0).getUpdateTypes()) {
      DiscordBroadcastUserUpdateType discordBroadcastUserUpdateType =
          DiscordUtils.convertUserUpdateType(userUpdateType);
      embeds.add(
          discordBroadcastUserUpdateType.generateUserUpdateEmbed(
              userUpdates, discordEmbedColorConfig));
    }

    return Message.builder().content("").embeds(embeds).build();
  }
}
