package io.enigmasolutions.webmonitor.authservice.services.web;

import io.enigmasolutions.webmonitor.authservice.models.discord.DiscordGuildMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DiscordClient {

    private final WebClient webClient;

    public DiscordClient(
            @Value("${discord.bot.token}") String botToken
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://discord.com/api")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, botToken);
                    httpHeaders.add(HttpHeaders.USER_AGENT, "EnigmaSolutions");
                })
                .build();
    }

    public DiscordGuildMember retrieveDiscordGuildMember(String guildId, String discordId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/guilds/{guildId}/members/{discordId}")
                        .build(guildId, discordId))
                .retrieve()
                .bodyToMono(DiscordGuildMember.class)
                .block();
    }
}
