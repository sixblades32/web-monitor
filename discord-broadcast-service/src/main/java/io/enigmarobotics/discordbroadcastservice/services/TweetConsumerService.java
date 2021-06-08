package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.dto.models.Embed;
import io.enigmarobotics.discordbroadcastservice.dto.models.Message;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Alert;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.TweetImage;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TweetConsumerService {

    private PostmanService postmanService;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;

    @Autowired
    TweetConsumerService(PostmanService postmanService, DiscordEmbedColorConfig discordEmbedColorConfig){
        this.postmanService = postmanService;
        this.discordEmbedColorConfig = discordEmbedColorConfig;
    }

    @KafkaListener(topics = "${kafka.tweet-consumer.topic}",
            groupId = "${kafka.tweet-consumer.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consume(Tweet tweet) {
        System.out.println("Received Message: " + tweet);

        List<Embed> embeds = tweet.getTweetType().generateTweetEmbed(tweet, discordEmbedColorConfig);

        Message message = Message.builder()
                .content("")
                .embeds(embeds)
                .build();

        postmanService.sendTwitterEmbed(message, tweet.getUserId());
    }

    @KafkaListener(topics = "${kafka.tweet-image-consumer.topic}",
            groupId = "${kafka.tweet-image-consumer.group-id}",
            containerFactory = "tweetImageKafkaListenerContainerFactory")
    public void consume(TweetImage tweetImage) {
        System.out.println("Received Message: " + tweetImage);

        List<Embed> embeds = tweetImage.getTweetType().generateImageEmbed(tweetImage, discordEmbedColorConfig);

        Message message = Message.builder()
                .content("")
                .embeds(embeds)
                .build();

        postmanService.sendTwitterEmbed(message, tweetImage.getUserId());
    }

    @KafkaListener(topics = "${kafka.alert-consumer.topic}"
            , groupId = "${kafka.alert-consumer.group-id}",
            containerFactory = "alertKafkaListenerContainerFactory")
    public void consume(Alert alert) {
        System.out.println("Received Message: " + alert);

        Embed alertEmbed = DiscordUtils.generateAlertEmbed(alert, discordEmbedColorConfig.getAlert());

        Message message = Message.builder()
                .content("")
                .embeds(Collections.singletonList(alertEmbed))
                .build();

        postmanService.sendAlertEmbed(message);
    }
}
