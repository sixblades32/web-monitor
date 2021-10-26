package io.enigmasolutions.staffmanagerbot.services.discord.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import io.enigmasolutions.staffmanagerbot.services.discord.EmbedGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public abstract class DiscordCommand {

  protected EmbedGenerator embedGenerator;

  public DiscordCommand(EmbedGenerator embedGenerator) {
    this.embedGenerator = embedGenerator;
  }

  public String getName() {
    return "default";
  }

  public abstract String getDescription();

  public abstract String getSyntax();

  public Message help(MessageCreateEvent event) {
    EmbedCreateFields.Field nameField =
        EmbedCreateFields.Field.of("Command Name:", getName(), false);
    EmbedCreateFields.Field descriptionField =
        EmbedCreateFields.Field.of("Command Description:", getDescription(), false);
    EmbedCreateFields.Field syntaxField =
        EmbedCreateFields.Field.of("Syntax:", "`" + getSyntax() + "`", false);

    List<EmbedCreateFields.Field> fields =
        new java.util.ArrayList<>(List.of(nameField, descriptionField, syntaxField));

    EmbedCreateFields.Field additionalField = generateAdditionalField();

    if (Objects.nonNull(additionalField)) {
      fields.add(additionalField);
    }

    return sendEmbed(event, embedGenerator.generateInfoEmbed("", fields));
  }
  ;

  public abstract Message handle(MessageCreateEvent event, List<String> args);

  protected abstract EmbedCreateFields.Field generateAdditionalField();

  protected Message sendEmbed(MessageCreateEvent event, EmbedCreateSpec embed) {
    return event
        .getMessage()
        .getChannel()
        .flatMap(messageChannel -> messageChannel.createMessage(embed))
        .block();
  }

  protected abstract Boolean isArgsValid(List<String> args);
}
