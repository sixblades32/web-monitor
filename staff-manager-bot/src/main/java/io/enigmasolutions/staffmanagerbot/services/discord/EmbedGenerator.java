package io.enigmasolutions.staffmanagerbot.services.discord;

import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmbedGenerator {
  @Value("${discord.color.valid}")
  public int validColor;
  @Value("${discord.color.invalid}")
  public int invalidColor;
  @Value("${discord.color.base}")
  public int baseColor;

  public EmbedCreateSpec generateValidEmbed(String description) {
    return EmbedCreateSpec.builder().title("Done!").description(description).color(Color.of(validColor)).build();
  }

  public EmbedCreateSpec generateInvalidEmbed(String description) {
    return EmbedCreateSpec.builder().title("Oops...").description(description).color(Color.of(invalidColor)).build();
  }

  public EmbedCreateSpec generateInfoEmbed(String description, List<EmbedCreateFields.Field> fields){
    return EmbedCreateSpec.builder().title("Info:").fields(fields).description(description).color(Color.of(baseColor)).build();
  }


}
