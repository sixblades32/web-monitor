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

@Service
@Slf4j
public class TweetConsumerService {

    private final PostmanService postmanService;
    private final DiscordEmbedColorConfig discordEmbedColorConfig;
    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

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
        Message videoMessage = generateVideoMessage(tweet);

        PROCESSING_EXECUTOR.execute(() -> postmanService.processLive(message));
        PROCESSING_EXECUTOR.execute(() -> processBaseVideoMessage(videoMessage));
    }

    @KafkaListener(topics = "${kafka.tweet-consumer-live-release.topic}",
            groupId = "${kafka.tweet-consumer-live-release.group-id}",
            containerFactory = "tweetKafkaListenerContainerFactory")
    public void consumeLiveReleaseTopic(Tweet tweet) {
        log.info("Received live release tweet message {}", tweet);

        Message message = generateTweetMessage(tweet);
        Message videoMessage = generateVideoMessage(tweet);

        PROCESSING_EXECUTOR.execute(() -> postmanService.processLive(message));
        PROCESSING_EXECUTOR.execute(() -> processLiveVideoMessage(videoMessage));
    }

    private Message generateTweetMessage(Tweet tweet) {

        DiscordBroadcastTweetType tweetType = DiscordUtils.convertTweetType(tweet.getType());
        List<Embed> embeds = tweetType.generateTweetEmbed(tweet, discordEmbedColorConfig);

        return Message.builder()
                .content("")
                .embeds(embeds)
                .build();
    }

    private void processBaseVideoMessage(Message videoMessage) {
        if (videoMessage == null) return;

        postmanService.processBase(videoMessage);
    }

    private void processLiveVideoMessage(Message videoMessage) {
        if (videoMessage == null) return;

        postmanService.processLive(videoMessage);
    }

    private Message generateVideoMessage(Tweet tweet) {

        if (tweet.getMedia().size() > 1 && tweet.getMedia().get(0).getType() == MediaType.PHOTO) return null;

        return Message.builder()
                .content(tweet.getMedia().get(0).getAnimation())
                .build();
    }
}
