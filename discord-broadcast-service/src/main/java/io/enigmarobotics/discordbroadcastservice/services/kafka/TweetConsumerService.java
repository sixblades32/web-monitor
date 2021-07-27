package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.MediaType;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public void consumeBaseTopic(Tweet tweet) throws InterruptedException {
        log.info("Received base tweet message {}", tweet);

        Message message = generateTweetMessage(tweet);

        CompletableFuture.runAsync(() -> postmanService.processCommon(message));
        CompletableFuture.runAsync(() -> processBaseVideoMessage(tweet));
    }

    @KafkaListener(topics = "${kafka.tweet-consumer-live-release.topic}",
            groupId = "${kafka.tweet-consumer-live-release.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(Tweet tweet) {
        log.info("Received live release tweet message {}", tweet);

        Message message = generateTweetMessage(tweet);
        postmanService.processAdvanced(message);
        processLiveVideoMessage(tweet);
    }

    private Message generateTweetMessage(Tweet tweet) {
        List<Embed> embeds = tweet.getType().generateTweetEmbed(tweet, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }

    private void processBaseVideoMessage(Tweet tweet){
        if (tweet.getMedia().size() != 1) return;
        if(tweet.getMedia().get(0).getType() == MediaType.PHOTO) return;

        Message videoMessage = generateVideoMessage(tweet);
        postmanService.processCommon(videoMessage);
    }

    private void processLiveVideoMessage(Tweet tweet){
        if (tweet.getMedia().size() != 1) return;
        if(tweet.getMedia().get(0).getType() == MediaType.PHOTO) return;

        Message videoMessage = generateVideoMessage(tweet);
        postmanService.processAdvanced(videoMessage);
    }

    private Message generateVideoMessage(Tweet tweet){
        return Message.builder()
                .content(tweet.getMedia().get(0).getAnimation())
                .build();
    }
}
