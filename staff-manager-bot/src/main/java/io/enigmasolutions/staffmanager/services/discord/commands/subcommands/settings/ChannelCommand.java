package io.enigmasolutions.staffmanager.services.discord.commands.subcommands.settings;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import io.enigmasolutions.staffmanager.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanager.services.web.DictionaryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class ChannelCommand extends SettingsSubcommand {

  private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

  private final DictionaryClient dictionaryClient;

  public ChannelCommand(EmbedGenerator embedGenerator, DictionaryClient dictionaryClient) {
    super(embedGenerator);
    this.dictionaryClient = dictionaryClient;
  }

  public String getName() {
    return "channel";
  }

  @Override
  public String getDescription() {
    return "Subcommand of Setting Command. Changes by id the channel for receiving staff messages";
  }

  @Override
  public String getSyntax() {
    return ".tmonitor settings channel <new-channel-id>";
  }

  @Override
  public Message handle(MessageCreateEvent event, List<String> args) {
    if (isArgsValid(args)) {
      String guildId = event.getGuildId().get().asString();
      String newChannelId = args.get(0);

      dictionaryClient.changeChannelId(guildId, newChannelId);
      return sendEmbed(
          event,
          embedGenerator.generateValidEmbed(
              "Channel for receiving monitor staffs' messages changed."));
    } else {
      return sendEmbed(
          event,
          embedGenerator.generateInvalidEmbed(
              "Something went wrong... Check number of arguments and argument content"));
    }
  }

  @Override
  protected EmbedCreateFields.Field generateAdditionalField() {
    return null;
  }

  @Override
  protected Boolean isArgsValid(List<String> args) {
    return args.size() == 1 && isNumeric(args.get(0));
  }

  private Boolean isNumeric(String channelId) {
    if (channelId == null) {
      return false;
    }
    return pattern.matcher(channelId).matches();
  }
}
