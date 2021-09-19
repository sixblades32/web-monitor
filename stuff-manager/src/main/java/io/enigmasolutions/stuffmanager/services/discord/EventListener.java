package io.enigmasolutions.stuffmanager.services.discord;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.discordjson.possible.Possible;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class EventListener {

    private AccessChecker accessChecker;
    @Value("${discord.token}")
    public String token;

    @Autowired
    EventListener(AccessChecker accessChecker){
        this.accessChecker = accessChecker;
    }

    public Mono<Void> login(){

        DiscordClient discordClient = DiscordClient.create(token);

        Mono<Void> login = discordClient.withGateway((GatewayDiscordClient gateway) -> {
            // ReadyEvent example
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        log.info("Logged in as {}{}", self.getUsername(), self.getDiscriminator());
                    }))
                    .then();

            // MessageCreateEvent example
            Mono<Void> handlePingCommand = gateway.on(MessageCreateEvent.class, event -> {

                Possible<Boolean> possibleBot = event.getMessage().getData().author().bot();

                Message message = event.getMessage();

                if (!accessChecker.isBot(possibleBot)) {
                    return message.getChannel()
                            .flatMap(channel -> channel.createMessage("pong!"));
                }

                return Mono.empty();
            }).then();

            return printOnLogin.and(handlePingCommand);
        });

        return login;
    }
}
