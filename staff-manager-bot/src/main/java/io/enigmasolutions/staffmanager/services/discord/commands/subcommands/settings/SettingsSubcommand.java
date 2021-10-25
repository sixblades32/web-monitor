package io.enigmasolutions.staffmanager.services.discord.commands.subcommands.settings;

import io.enigmasolutions.staffmanager.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanager.services.discord.commands.SettingsCommand;
import org.springframework.stereotype.Component;

@Component
public abstract class SettingsSubcommand extends SettingsCommand {
  public SettingsSubcommand(EmbedGenerator embedGenerator) {
    super(embedGenerator);
  }
}
