package io.enigmasolutions.stuffmanager.services.discord;

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
import io.enigmasolutions.stuffmanager.services.kafka.KafkaProducer;
import io.enigmasolutions.stuffmanager.services.utils.UrlExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class EventListener {

    private final AccessChecker accessChecker;
    private final UrlExtractor urlExtractor;
    private final KafkaProducer kafkaProducer;
    @Value("${discord.token}")
    public String token;

    @Autowired
    EventListener(AccessChecker accessChecker,
                  KafkaProducer kafkaProducer,
                  UrlExtractor urlExtractor) {
        this.accessChecker = accessChecker;
        this.kafkaProducer = kafkaProducer;
        this.urlExtractor = urlExtractor;
    }

    public Mono<Void> login() {

        DiscordClient discordClient = DiscordClient.create(token);

        Mono<Void> login = discordClient.withGateway((GatewayDiscordClient gateway) -> {

            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        log.info("Logged in as {}{}", self.getUsername(), self.getDiscriminator());
                    }))
                    .then();

            Mono<Void> handlePingCommand = gateway.on(MessageCreateEvent.class, event -> {

                Message message = event.getMessage();

                if (message.getAuthor().isPresent() && !message.getAuthor().get().isBot()) {

                    CustomerDiscordGuild customerDiscordGuild = accessChecker.getRequiredCustomer(message);

                    Staff staffMessage = generateStaffMessage(message);

                    if (customerDiscordGuild != null)
                        kafkaProducer.sendStaffMessage(staffMessage, customerDiscordGuild.getCustomerId());
                }

                return Mono.empty();
            }).then();

            return printOnLogin.and(handlePingCommand);
        });

        return login;
    }

    private Staff generateStaffMessage(Message message) {

        DiscordUser discordUser = null;

        if (message.getAuthor().isPresent()) {
            discordUser = DiscordUser.builder()
                    .icon(message.getAuthor().get().getAvatarUrl())
                    .name(message.getAuthor().get().getUsername())
                    .tag(message.getAuthor().get().getTag())
                    .build();
        }

        List<String> detectedUrls = urlExtractor.extractURL(message.getContent());

        String content = message.getContent();

        if (!detectedUrls.isEmpty()) {
            for (String url : detectedUrls) {
                content = content.replace(url, "");
            }
        }

        return Staff.builder()
                .type(StaffType.PLAIN)
                .text(content)
                .discordUser(discordUser)
                .detectedUrls(detectedUrls)
                .build();

    }
}
