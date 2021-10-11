package io.enigmarobotics.discordbroadcastservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "discord.embed.color")
public class DiscordEmbedColorConfig {

  private int retweet;
  private int tweet;
  private int reply;
  private int alert;
  private int userUpdates;
}
