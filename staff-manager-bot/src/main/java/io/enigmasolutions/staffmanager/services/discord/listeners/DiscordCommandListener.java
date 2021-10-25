package io.enigmasolutions.staffmanager.services.discord.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.enigmasolutions.staffmanager.services.discord.commands.DiscordCommand;
import io.enigmasolutions.staffmanager.services.discord.commands.subcommands.settings.SettingsSubcommand;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DiscordCommandListener {
  public final Collection<DiscordCommand> commands;
  public final Collection<SettingsSubcommand> settingsSubcommands;

  public DiscordCommandListener(ApplicationContext applicationContext) {
    commands = applicationContext.getBeansOfType(DiscordCommand.class).values();
    settingsSubcommands = applicationContext.getBeansOfType(SettingsSubcommand.class).values();
  }

  public void handle(String commandName, MessageCreateEvent event, List<String> args) {

    Optional<DiscordCommand> discordCommand = commands.stream().filter(command -> command.getName().equalsIgnoreCase(commandName)).findFirst();

    discordCommand.ifPresent(command -> command.handle(event, args));
  }
}
