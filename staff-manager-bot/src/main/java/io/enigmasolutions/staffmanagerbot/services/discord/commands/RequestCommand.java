package io.enigmasolutions.staffmanagerbot.services.discord.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateFields;
import io.enigmasolutions.broadcastmodels.FollowRequest;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.staffmanagerbot.services.discord.EmbedGenerator;
import io.enigmasolutions.staffmanagerbot.services.web.TwitterMonitorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Component
@Slf4j
public class RequestCommand extends DiscordCommand {

  private final TwitterMonitorClient twitterMonitorClient;

  public RequestCommand(EmbedGenerator embedGenerator, TwitterMonitorClient twitterMonitorClient) {
    super(embedGenerator);
    this.twitterMonitorClient = twitterMonitorClient;
  }

  @Override
  public String getName() {
    return "request";
  }

  @Override
  public String getDescription() {
    return "Creates follow request for Twitter Monitor";
  }

  @Override
  public String getSyntax() {
    return ".tmonitor request <twitter-username>";
  }

  @Override
  public Message handle(MessageCreateEvent event, List<String> args) {
    return createFollowRequest(event, args);
  }

  @Override
  protected EmbedCreateFields.Field generateAdditionalField() {
    return null;
  }

  @Override
  protected Boolean isArgsValid(List<String> args) {
    return args.size() == 1 && args.get(0).length() <= 15 && args.get(0).length() >= 4;
  }

  private Message createFollowRequest(MessageCreateEvent event, List<String> args) {
    if (isArgsValid(args)) {

      FollowRequest followRequest =
          FollowRequest.builder()
              .twitterUser(TwitterUser.builder().login(args.get(0)).build())
              .guildName(event.getGuild().block().getName())
              .build();

      try {
        twitterMonitorClient.createFollowRequest(followRequest);
      } catch (HttpClientErrorException exception) {
        log.error(exception.getMessage());

        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
          return sendEmbed(
              event,
              embedGenerator.generateInvalidEmbed("Twitter User with such username not found!"));
        } else if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
          return sendEmbed(
              event,
              embedGenerator.generateInvalidEmbed(
                  "Twitter User already added to the monitor's account pool!"));
        }
      }

      return sendEmbed(event, embedGenerator.generateValidEmbed("Request created!"));
    } else {
      return sendEmbed(
          event,
          embedGenerator.generateInvalidEmbed(
              "Something went wrong... Check number of arguments and argument content"));
    }
  }
}
