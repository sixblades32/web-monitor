package io.enigmasolutions.staffmanagerbot.services.discord.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import io.enigmasolutions.staffmanagerbot.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanagerbot.services.discord.commands.subcommands.settings.SettingsSubcommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.enigmasolutions.staffmanagerbot.services.discord.listeners.DiscordEventListener.discordCommandListener;

@Component
public class SettingsCommand extends DiscordCommand {

  @Autowired
  public SettingsCommand(EmbedGenerator embedGenerator) {
    super(embedGenerator);
  }

  @Override
  public String getName() {
    return "settings";
  }

  @Override
  public String getDescription() {
    return "Sets guild configuration for Staff Manager Bot";
  }

  @Override
  public String getSyntax() {
    return ".tmonitor settings <subcommand> [value]";
  }

  @Override
  public Message handle(MessageCreateEvent event, List<String> args) {
    if (!isArgsValid(args))
      return sendEmbed(event, embedGenerator.generateInvalidEmbed("Need to enter the subcommand!"));

    Optional<SettingsSubcommand> discordSubcommand =
        discordCommandListener.settingsSubcommands.stream()
            .filter(command -> command.getName().equalsIgnoreCase(args.get(0)))
            .findFirst();

    args.remove(0);

    if (discordSubcommand.isPresent()) {
      return discordSubcommand.get().handle(event, args);
    } else {
      return sendEmbed(event, embedGenerator.generateInvalidEmbed("No such setting available!"));
    }
  }

  @Override
  protected EmbedCreateFields.Field generateAdditionalField() {
    Collection<SettingsSubcommand> subcommands =
        discordCommandListener.settingsSubcommands;

    StringBuilder subcommandsValue = new StringBuilder();

    for (DiscordCommand command : subcommands) {
      if (subcommandsValue.toString().equals("")) {
        subcommandsValue.append("`").append(command.getName()).append("`");
      } else {
        subcommandsValue.append(", ").append("`").append(command.getName()).append("`");
      }
    }

    return EmbedCreateFields.Field.of("Available Subcommands:", subcommandsValue.toString(), true);
  }

  @Override
  protected Boolean isArgsValid(List<String> args) {
    return args.size() > 0;
  }
}
