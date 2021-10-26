package io.enigmasolutions.staffmanagerbot.services.discord.commands.subcommands.settings;

import io.enigmasolutions.staffmanagerbot.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanagerbot.services.discord.commands.SettingsCommand;
import org.springframework.stereotype.Component;

@Component
public abstract class SettingsSubcommand extends SettingsCommand {
  public SettingsSubcommand(EmbedGenerator embedGenerator) {
    super(embedGenerator);
  }
}
