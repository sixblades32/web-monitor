package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TweetType {

    TWEET {
        @Override
        public List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig) {
            Embed imageEmbed = DiscordUtils.generateTweetImageEmbed(tweetImage, discordEmbedColorConfig.getTweet());

            return Collections.singletonList(imageEmbed);
        }

        @Override
        public List<Embed> generateTweetEmbed(BroadcastTweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return DiscordUtils.generateTweetEmbed(tweet, discordEmbedColorConfig.getTweet());
        }

        @Override
        public Embed generateTweetEmbed(BroadcastTweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }
    },
    RETWEET {
        @Override
        public List<Embed> generateTweetEmbed(BroadcastTweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            List<Embed> embeds = new ArrayList<>();
            Embed mainTweet = DiscordUtils.generateRetweetEmbed(tweet, discordEmbedColorConfig.getRetweet());
            List<Embed> retweetedTweet = DiscordUtils.generateTweetFromRetweetEmbed(tweet.getRetweeted(), tweet.getUserName(), discordEmbedColorConfig.getRetweet());

            embeds.add(mainTweet);
            embeds.addAll(retweetedTweet);

            return embeds;
        }

        @Override
        public Embed generateTweetEmbed(BroadcastTweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }

        @Override
        public List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig) {
            Embed imageEmbed = DiscordUtils.generateRetweetImageEmbed(tweetImage, discordEmbedColorConfig.getRetweet());

            return Collections.singletonList(imageEmbed);
        }
    },
    REPLY {
        @Override
        public List<Embed> generateTweetEmbed(BroadcastTweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            Embed replyEmbed = DiscordUtils.generateReplyEmbed(tweet, discordEmbedColorConfig.getReply());

            return Collections.singletonList(replyEmbed);
        }

        @Override
        public Embed generateTweetEmbed(BroadcastTweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }

        @Override
        public List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }
    };

    public abstract List<Embed> generateTweetEmbed(BroadcastTweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig);

    public abstract Embed generateTweetEmbed(BroadcastTweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig);

    public abstract List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig);
}
