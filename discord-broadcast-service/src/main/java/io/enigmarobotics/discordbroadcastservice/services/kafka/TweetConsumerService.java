package io.enigmarobotics.discordbroadcastservice.services.kafka;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastTweetType;
import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.MediaType;
import io.enigmasolutions.broadcastmodels.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.enigmasolutions.broadcastmodels.MediaType.ANIMATED_GIF;
import static io.enigmasolutions.broadcastmodels.MediaType.VIDEO;

@Service
@Slf4j
public class TweetConsumerService {

    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();
    private final List<MediaType> mediaTypes = List.of(VIDEO, ANIMATED_GIF);

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
    public void consumeBaseTopic(Tweet tweet) {
        log.info("Received base tweet message {}", tweet);

        PROCESSING_EXECUTOR.execute(() -> {
            Message message = generateTweetMessage(tweet);
            postmanService.processLive(message);
        });
        PROCESSING_EXECUTOR.execute(() -> {
            Message videoMessage = generateVideoMessage(tweet);
            if (videoMessage != null) {
                postmanService.processBase(videoMessage);
            }
        });
    }

    @KafkaListener(topics = "${kafka.tweet-consumer-live-release.topic}",
            groupId = "${kafka.tweet-consumer-live-release.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(Tweet tweet) {
        log.info("Received live release tweet message {}", tweet);

        PROCESSING_EXECUTOR.execute(() -> {
            Message message = generateTweetMessage(tweet);
            postmanService.processLive(message);
        });
        PROCESSING_EXECUTOR.execute(() -> {
            Message videoMessage = generateVideoMessage(tweet);
            if (videoMessage != null) {
                postmanService.processLive(videoMessage);
            }
        });
    }

    private Message generateTweetMessage(Tweet tweet) {
        DiscordBroadcastTweetType tweetType = DiscordUtils.convertTweetType(tweet.getType());
        List<Embed> embeds = tweetType.generateTweetEmbed(tweet, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }

    private Message generateVideoMessage(Tweet tweet) {
        if (tweet.getMedia().isEmpty() || !mediaTypes.contains(tweet.getMedia().get(0).getType())) {
            return null;
        }

        return Message.builder()
                .content(tweet.getMedia().get(0).getAnimation())
                .build();
    }
}
