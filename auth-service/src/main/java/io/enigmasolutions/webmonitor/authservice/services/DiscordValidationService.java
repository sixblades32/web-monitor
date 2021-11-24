package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.webmonitor.authservice.models.discord.DiscordGuildMember;
import io.enigmasolutions.webmonitor.authservice.services.web.DictionaryServiceClient;
import io.enigmasolutions.webmonitor.authservice.services.web.DiscordClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Service
public class DiscordValidationService {

  private final DictionaryServiceClient dictionaryServiceClient;
  private final DiscordClient discordClient;

  public DiscordValidationService(DictionaryServiceClient dictionaryServiceClient,
      DiscordClient discordClient) {
    this.dictionaryServiceClient = dictionaryServiceClient;
    this.discordClient = discordClient;
  }

  public boolean isUserContainsMutualGuild(String discordId, String customerId) {
    try {
      CustomerDiscordGuild customerDiscordGuild =
          dictionaryServiceClient.retrieveCustomerDiscordGuild(customerId);
      DiscordGuildMember discordGuildMember =
          discordClient.retrieveDiscordGuildMember(customerDiscordGuild.getGuildId(), discordId);

      return CollectionUtils.containsAny(customerDiscordGuild.getUsersRoles(),
          discordGuildMember.getRoles());
    } catch (WebClientException e) {
      log.error(e.getMessage());
    } catch (Exception e) {
      log.error("Unexpected exemption", e);
    }

    return false;
  }
}
