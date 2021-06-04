package io.enigmarobotics.discordbroadcastservice.controllers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.db.models.Customer;
import io.enigmarobotics.discordbroadcastservice.dto.models.Embed;
import io.enigmarobotics.discordbroadcastservice.dto.models.Message;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Alert;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.TweetImage;
import io.enigmarobotics.discordbroadcastservice.services.DiscordClient;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/broadcast")
public class BroadcastController {

    private final PostmanService postmanService;
    private final DiscordClient discordClient;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;

    @Autowired
    public BroadcastController(
            PostmanService postmanService,
            DiscordClient discordClient,
            DiscordEmbedColorConfig discordEmbedColorConfig
    ) {
        this.postmanService = postmanService;
        this.discordClient = discordClient;
        this.discordEmbedColorConfig = discordEmbedColorConfig;
    }

    @PostMapping("/tweet")
    public void sendEmbed(@RequestBody Tweet tweet) {

        List<Embed> embeds = tweet.getTweetType().generateTweetEmbed(tweet, discordEmbedColorConfig);

        Message message = Message.builder()
                .content("")
                .embeds(embeds)
                .build();

        postmanService.sendTwitterEmbed(message, tweet.getUserId());
    }

    @PostMapping("/image")
    public void sendEmbed(@RequestBody TweetImage tweetImage) {

        List<Embed> embeds = tweetImage.getTweetType().generateImageEmbed(tweetImage, discordEmbedColorConfig);

        Message message = Message.builder()
                .content("")
                .embeds(embeds)
                .build();

        postmanService.sendTwitterEmbed(message, tweetImage.getUserId());
    }

    @PostMapping("/alert")
    public void sendEmbed(@RequestBody Alert alert) {

        Embed alertEmbed = DiscordUtils.generateAlertEmbed(alert, discordEmbedColorConfig.getAlert());

        Message message = Message.builder()
                .content("")
                .embeds(Collections.singletonList(alertEmbed))
                .build();



       postmanService.sendAlertEmbed(message);
    }
}
