package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.BroadcastRecognition;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.BroadcastTweet;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TweetConsumerService {

    private final PostmanService postmanService;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;

    @Autowired
    TweetConsumerService(PostmanService postmanService, DiscordEmbedColorConfig discordEmbedColorConfig) {
        this.postmanService = postmanService;
        this.discordEmbedColorConfig = discordEmbedColorConfig;
    }

    @KafkaListener(topics = "${kafka.tweet-consumer-base.topic}",
            groupId = "${kafka.tweet-consumer-base.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consumeBaseTopic(BroadcastTweet tweet) {

        log.info("Received Base Tweet Message from: " + tweet.getUserName());

        Message message = generateTweetMessage(tweet);

        postmanService.processCommon(message);
    }

    @KafkaListener(topics = "${kafka.tweet-consumer-live-release.topic}",
            groupId = "${kafka.tweet-consumer-live-release.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(BroadcastTweet tweet) {

        log.info("Received Live Release Tweet Message from: " + tweet.getUserName());

        Message message = generateTweetMessage(tweet);

        postmanService.processAdvanced(message);
    }

    public void consumeRecognition(BroadcastRecognition recognition){

        log.info("Received Recognition Result from" + recognition.getUserName());

        Message message = generateRecognitionMessage(recognition);
        postmanService.sendRecognition(message);
    }

    private Message generateTweetMessage(BroadcastTweet tweet){
        List<Embed> embeds = tweet.getType().generateTweetEmbed(tweet, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }

    private Message generateRecognitionMessage(BroadcastRecognition recognition) {

        List<Embed> embeds = recognition.getTweetType().generateRecognitionEmbed(recognition, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }


}
