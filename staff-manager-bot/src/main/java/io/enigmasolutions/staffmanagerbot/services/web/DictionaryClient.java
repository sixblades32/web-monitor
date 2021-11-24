package io.enigmasolutions.staffmanagerbot.services.web;

import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class DictionaryClient {

  private final WebClient webClient;

  public DictionaryClient(@Value("${dictionary-service.url}") String dictionaryServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(dictionaryServiceUrl)
        .build();
  }

  public List<CustomerDiscordGuild> getCustomerDiscordGuilds() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/customers/guilds/all")
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<CustomerDiscordGuild>>() {
        })
        .block();
  }

  public String changeChannelId(String guildId, String channelId){
    return webClient
            .put()
            .uri(uriBuilder -> uriBuilder.path("/customers/guilds/" + guildId + "/channels/" + channelId).build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
  }
}
