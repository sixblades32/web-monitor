package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.broadcastmodels.TweetImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DiscordBroadcastTweetType {

  TWEET {
    @Override
    public List<Embed> generateImageEmbed(TweetImage tweetImage,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      Embed imageEmbed = DiscordUtils.generateTweetImageEmbed(tweetImage,
          discordEmbedColorConfig.getTweet());

      return Collections.singletonList(imageEmbed);
    }

    @Override
    public List<Embed> generateTweetEmbed(Tweet tweet,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      return DiscordUtils.generateTweetEmbed(tweet, discordEmbedColorConfig.getTweet());
    }

    @Override
    public List<Embed> generateRecognitionEmbed(
        Recognition recognition,
        DiscordEmbedColorConfig discordEmbedColorConfig
    ) {
      Embed recognitionEmbed = DiscordUtils.generateTweetRecognitionEmbed(
          recognition,
          discordEmbedColorConfig.getTweet()
      );

      return Collections.singletonList(recognitionEmbed);
    }


  },
  RETWEET {
    @Override
    public List<Embed> generateTweetEmbed(Tweet tweet,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      List<Embed> embeds = new ArrayList<>();
      List<Embed> retweetedTweet;
      Embed coMainTweet = null;

      Embed mainTweet = DiscordUtils.generateRetweetEmbed(tweet,
          discordEmbedColorConfig.getRetweet());

      if (tweet.getRetweeted().getRetweeted() != null) {
        coMainTweet = DiscordUtils.generateRetweetEmbed(tweet.getRetweeted(),
            discordEmbedColorConfig.getRetweet());
        retweetedTweet = DiscordUtils.generateTweetFromRetweetEmbed(
            tweet.getRetweeted().getRetweeted(),
            tweet.getRetweeted().getUser().getLogin(),
            discordEmbedColorConfig.getRetweet());
      } else {
        retweetedTweet = DiscordUtils.generateTweetFromRetweetEmbed(
            tweet.getRetweeted(),
            tweet.getUser().getLogin(),
            discordEmbedColorConfig.getRetweet()
        );
      }

      embeds.add(mainTweet);
      if (coMainTweet != null) {
        embeds.add(coMainTweet);
      }
      embeds.addAll(retweetedTweet);

      return embeds;
    }

    @Override
    public List<Embed> generateImageEmbed(TweetImage tweetImage,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      Embed imageEmbed = DiscordUtils.generateRetweetImageEmbed(tweetImage,
          discordEmbedColorConfig.getRetweet());

      return Collections.singletonList(imageEmbed);
    }

    @Override
    public List<Embed> generateRecognitionEmbed(
        Recognition recognition,
        DiscordEmbedColorConfig discordEmbedColorConfig
    ) {
      Embed recognitionEmbed = DiscordUtils
          .generateRetweetRecognitionEmbed(recognition, discordEmbedColorConfig.getRetweet());

      return Collections.singletonList(recognitionEmbed);
    }
  },
  REPLY {
    @Override
    public List<Embed> generateTweetEmbed(Tweet tweet,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      Embed replyEmbed = DiscordUtils.generateReplyEmbed(tweet, discordEmbedColorConfig.getReply());

      return Collections.singletonList(replyEmbed);
    }

    @Override
    public List<Embed> generateImageEmbed(TweetImage tweetImage,
        DiscordEmbedColorConfig discordEmbedColorConfig) {
      return null;
    }

    @Override
    public List<Embed> generateRecognitionEmbed(
        Recognition recognition,
        DiscordEmbedColorConfig discordEmbedColorConfig
    ) {
      Embed recognitionEmbed = DiscordUtils
          .generateReplyRecognitionEmbed(recognition, discordEmbedColorConfig.getReply());

      return Collections.singletonList(recognitionEmbed);
    }
  };

  public abstract List<Embed> generateTweetEmbed(
      Tweet tweet,
      DiscordEmbedColorConfig discordEmbedColorConfig
  );

  public abstract List<Embed> generateImageEmbed(
      TweetImage tweetImage,
      DiscordEmbedColorConfig discordEmbedColorConfig
  );

  public abstract List<Embed> generateRecognitionEmbed(
      Recognition recognition,
      DiscordEmbedColorConfig discordEmbedColorConfig
  );
}
