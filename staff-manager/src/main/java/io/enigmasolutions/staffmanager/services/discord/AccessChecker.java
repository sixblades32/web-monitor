package io.enigmasolutions.staffmanager.services.discord;

import discord4j.core.object.entity.Message;
import discord4j.discordjson.Id;
import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.staffmanager.services.CustomerDiscordGuildCache;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AccessChecker {

  private CustomerDiscordGuildCache customerDiscordGuildCache;

  public AccessChecker(CustomerDiscordGuildCache customerDiscordGuildCache) {
    this.customerDiscordGuildCache = customerDiscordGuildCache;
  }

  public Optional<CustomerDiscordGuild> getRequiredCustomer(Message message) {

    String guildId = message.getData().guildId().get().asString();
    String channelId = message.getChannelId().asString();

    List<String> userRoles = message.getData().member().get().roles().stream().map(Id::asString)
        .collect(Collectors.toList());

    List<CustomerDiscordGuild> customers = customerDiscordGuildCache.getCustomers();

    Optional<CustomerDiscordGuild> requiredCustomer = customers.stream()
        .filter(customerDiscordGuild ->
            customerDiscordGuild.getGuildId().equals(guildId) && customerDiscordGuild.getChannelId()
                .equals(channelId) && customerDiscordGuild.getModeratorsRoles().stream()
                .anyMatch(userRoles::contains)
        ).findFirst();

    return requiredCustomer;
  }


}
