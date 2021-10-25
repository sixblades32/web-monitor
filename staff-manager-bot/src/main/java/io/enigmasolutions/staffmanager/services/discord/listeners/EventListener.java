package io.enigmasolutions.staffmanager.services.discord.listeners;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import io.enigmasolutions.broadcastmodels.DiscordUser;
import io.enigmasolutions.broadcastmodels.Staff;
import io.enigmasolutions.broadcastmodels.StaffType;
import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.staffmanager.services.discord.AccessChecker;
import io.enigmasolutions.staffmanager.services.kafka.KafkaProducer;
import io.enigmasolutions.staffmanager.services.utils.UrlExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventListener {

  private final AccessChecker accessChecker;
  private final UrlExtractor urlExtractor;
  private final KafkaProducer kafkaProducer;

  @Value("${discord.token}")
  public String token;

  @Value("${discord.command}")
  public String mainCommand;

  @Autowired
  EventListener(
      AccessChecker accessChecker, KafkaProducer kafkaProducer, UrlExtractor urlExtractor) {
    this.accessChecker = accessChecker;
    this.kafkaProducer = kafkaProducer;
    this.urlExtractor = urlExtractor;
  }

  public Mono<Void> login(ApplicationContext springContext) {
    DiscordClient discordClient = DiscordClient.create(token);
    DiscordCommandListener discordCommandListener = new DiscordCommandListener(springContext);

    return discordClient.withGateway(
        (GatewayDiscordClient gateway) -> {
          Mono<Void> printOnLogin =
              gateway
                  .on(
                      ReadyEvent.class,
                      event ->
                          Mono.fromRunnable(
                              () -> {
                                final User self = event.getSelf();
                                log.info(
                                    "Logged in as {}{}",
                                    self.getUsername(),
                                    self.getDiscriminator());
                              }))
                  .then();

          Mono<Void> handleStaffMessages =
              gateway
                  .on(
                      MessageCreateEvent.class,
                      event -> {
                        Message message = event.getMessage();

                        if (message.getAuthor().isPresent() && !message.getAuthor().get().isBot()) {
                          Optional<CustomerDiscordGuild> customerDiscordGuild =
                              accessChecker.getRequiredCustomer(message);

                          if (message.getContent().startsWith(mainCommand)) {
                            List<String> args =
                                new LinkedList<>(
                                    Arrays.asList(
                                        message
                                            .getContent()
                                            .substring(mainCommand.length())
                                            .trim()
                                            .split("\\s+")));

                            String command = args.remove(0);
                            discordCommandListener.handle(command, event, args);
                          } else {

                            Staff staffMessage = generateStaffMessage(message);
                            customerDiscordGuild.ifPresent(
                                discordGuild ->
                                    kafkaProducer.sendStaffMessage(
                                        staffMessage, discordGuild.getCustomerId()));
                          }
                        }

                        return Mono.empty();
                      })
                  .then();

          return printOnLogin.and(handleStaffMessages);
        });
  }

  private Staff generateStaffMessage(Message message) {

    DiscordUser discordUser = null;

    if (message.getAuthor().isPresent()) {
      discordUser =
          DiscordUser.builder()
              .icon(message.getAuthor().get().getAvatarUrl())
              .name(message.getAuthor().get().getUsername())
              .tag(message.getAuthor().get().getTag())
              .build();
    }

    List<String> detectedUrls = urlExtractor.extractURL(message.getContent());

    String content = message.getContent();

    return Staff.builder()
        .type(StaffType.PLAIN)
        .text(content)
        .discordUser(discordUser)
        .detectedUrls(detectedUrls)
        .build();
  }
}
