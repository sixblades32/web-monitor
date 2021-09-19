package io.enigmasolutions.stuffmanager.services.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.discordjson.Id;
import discord4j.discordjson.possible.Possible;
import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.stuffmanager.services.CustomerDiscordGuildCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessChecker {

    private CustomerDiscordGuildCache customerDiscordGuildCache;

    public AccessChecker(CustomerDiscordGuildCache customerDiscordGuildCache) {
        this.customerDiscordGuildCache = customerDiscordGuildCache;
    }

    public CustomerDiscordGuild getRequiredCustomer(Message message){

        CustomerDiscordGuild requiredCustomer = null;

        String guildId = message.getData().guildId().get().asString();

        List<String> userRoles = message.getData().member().get().roles().stream().map(Id::asString).collect(Collectors.toList());

        List<CustomerDiscordGuild> customers = customerDiscordGuildCache.getCustomers();

        List<CustomerDiscordGuild> filteredCustomersList = customers.stream().filter(customerDiscordGuild ->
            customerDiscordGuild.getGuildId().equals(guildId) && customerDiscordGuild.getModeratorsRoles().stream().anyMatch(userRoles::contains)
        ).collect(Collectors.toList());

        if(!filteredCustomersList.isEmpty()){
            requiredCustomer = filteredCustomersList.get(0);
        }

        return requiredCustomer;
    }


}
