package io.enigmarobotics.discordbroadcastservice.dto.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.dto.models.Embed;
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
        public List<Embed> generateTweetEmbed(Tweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            Embed tweetEmbed = DiscordUtils.generateTweetEmbed(tweet, discordEmbedColorConfig.getTweet());

            return Collections.singletonList(tweetEmbed);
        }

        @Override
        public Embed generateTweetEmbed(Tweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }
    },
    RETWEET {
        @Override
        public List<Embed> generateTweetEmbed(Tweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            List<Embed> embeds = new ArrayList<>();
            Embed mainTweet = DiscordUtils.generateRetweetEmbed(tweet, discordEmbedColorConfig.getRetweet());
            Embed retweetedTweet = DiscordUtils.generateTweetFromRetweetEmbed(tweet.getRetweeted(), tweet.getUserName(), discordEmbedColorConfig.getRetweet());

            embeds.add(mainTweet);
            embeds.add(retweetedTweet);

            return  embeds;
        }

        @Override
        public Embed generateTweetEmbed(Tweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }

        @Override
        public List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }
    },
    REPLY {
        @Override
        public List<Embed> generateTweetEmbed(Tweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig) {
            Embed replyEmbed = DiscordUtils.generateReplyEmbed(tweet, discordEmbedColorConfig.getReply());

            return Collections.singletonList(replyEmbed);
        }

        @Override
        public Embed generateTweetEmbed(Tweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }

        @Override
        public List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig) {
            return null;
        }
    };

    public abstract List<Embed> generateTweetEmbed(Tweet tweet, DiscordEmbedColorConfig discordEmbedColorConfig);

    public abstract Embed generateTweetEmbed(Tweet tweet, String retweetedBy, DiscordEmbedColorConfig discordEmbedColorConfig);

    public abstract List<Embed> generateImageEmbed(TweetImage tweetImage, DiscordEmbedColorConfig discordEmbedColorConfig);
}
