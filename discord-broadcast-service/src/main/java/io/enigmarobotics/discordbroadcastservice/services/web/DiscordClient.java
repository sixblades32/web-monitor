package io.enigmarobotics.discordbroadcastservice.services.web;

import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DiscordClient {

  private final WebClient webClient;

  public DiscordClient() {
    this.webClient = WebClient.builder()
        .build();
  }

  public void sendEmbed(String webhookUrl, Message message) {
    webClient.post()
        .uri(webhookUrl)
        .bodyValue(message)
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
