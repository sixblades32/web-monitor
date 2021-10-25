package io.enigmasolutions.staffmanager.services.discord.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import io.enigmasolutions.staffmanager.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanager.services.discord.commands.subcommands.settings.SettingsSubcommand;
import io.enigmasolutions.staffmanager.services.discord.listeners.DiscordCommandListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.enigmasolutions.staffmanager.StaffManagerApplication.springContext;

@Component
public class HelpCommand extends DiscordCommand {

  public HelpCommand(EmbedGenerator embedGenerator) {
    super(embedGenerator);
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Uses for get extended information on a command(subcommand)!";
  }

  @Override
  public String getSyntax() {
    return ".tmonitor help <command>";
  }

  @Override
  public Message handle(MessageCreateEvent event, List<String> args) {
    if (args.isEmpty()) {
      return help(event);
    } else {
      Collection<DiscordCommand> commands = new DiscordCommandListener(springContext).commands;
      Optional<DiscordCommand> discordCommand =
          commands.stream()
              .filter(command -> command.getName().equalsIgnoreCase(args.get(0)))
              .findFirst();

      return discordCommand.map(command -> command.help(event)).orElse(null);
    }
  }

  @Override
  protected EmbedCreateFields.Field generateAdditionalField() {
    Collection<DiscordCommand> commands = new DiscordCommandListener(springContext).commands;

    StringBuilder commandsValue = new StringBuilder();

    for (DiscordCommand command : commands) {
      if (command.getName().equals("help")) continue;

      if (commandsValue.toString().equals("")) {
        commandsValue.append("`").append(command.getName()).append("`");
      } else {
        commandsValue.append(", ").append("`").append(command.getName()).append("`");
      }
    }

    return EmbedCreateFields.Field.of("Available Commands:", commandsValue.toString(), true);
  }

  @Override
  protected Boolean isArgsValid(List<String> args) {
    return args.isEmpty();
  }
}
