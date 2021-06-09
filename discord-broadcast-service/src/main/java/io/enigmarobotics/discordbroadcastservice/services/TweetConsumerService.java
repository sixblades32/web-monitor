package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.Alert;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.TweetImage;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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

        log.info("Received Tweet Message from: " + tweet.getUserName());

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

        log.info("Received Tweet Image Message from: " + tweetImage.getUserName());


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

        log.info("Received Alert Message");

        Embed alertEmbed = DiscordUtils.generateAlertEmbed(alert, discordEmbedColorConfig.getAlert());

        Message message = Message.builder()
                .content("")
                .embeds(Collections.singletonList(alertEmbed))
                .build();

        postmanService.sendAlertEmbed(message);
    }
}
