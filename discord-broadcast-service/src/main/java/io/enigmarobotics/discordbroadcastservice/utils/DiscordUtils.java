package io.enigmarobotics.discordbroadcastservice.utils;

import io.enigmarobotics.discordbroadcastservice.domain.models.Author;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Field;
import io.enigmarobotics.discordbroadcastservice.domain.models.Footer;
import io.enigmarobotics.discordbroadcastservice.domain.models.Image;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastRecognitionType;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastTweetType;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.DiscordBroadcastUserUpdateType;
import io.enigmasolutions.broadcastmodels.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DiscordUtils {

  private DiscordUtils() {}

  public static List<Embed> generateTweetEmbed(Tweet tweet, int embedColor) {
    String description;

    if (!tweet.getText().equals("")) {
      description =
          "**[©"
              + tweet.getUser().getLogin()
              + "]("
              + tweet.getTweetUrl()
              + ")**\n"
              + "> "
              + tweet.getText().replaceAll("\\R", "\n> ");
    } else {
      description = "**[©" + tweet.getUser().getLogin() + "](" + tweet.getTweetUrl() + ")**\n";
    }

    List<Field> fields = new ArrayList<>();
    List<Embed> embeds = new LinkedList<>();
    Image image = null;

    Author author =
        Author.builder()
            .name(tweet.getUser().getLogin())
            .url(tweet.getTweetUrl())
            .iconUrl(tweet.getUser().getIcon())
            .build();

    Footer footer = Footer.builder().text("TWEET — " + tweet.getUser().getLogin()).build();

    List<Media> photos =
        tweet.getMedia().stream()
            .filter(img -> img.getType().equals(MediaType.PHOTO))
            .collect(Collectors.toList());
    int photosSize = photos.size();

    if (photosSize > 0) {
      image = Image.builder().url(photos.get(0).getStatical()).build();
    }

    Field additionalInfoField =
        Field.builder()
            .inline(false)
            .name("**ADDITIONAL INFO**")
            .value(
                "[**Retweets**]("
                    + tweet.getRetweetsUrl()
                    + ") • [**Following**]("
                    + tweet.getFollowsUrl()
                    + ") • [**Likes**]("
                    + tweet.getLikesUrl()
                    + ")")
            .build();

    fields.add(additionalInfoField);

    embeds.add(
        Embed.builder()
            .author(author)
            .footer(footer)
            .title(String.valueOf(tweet.getType()))
            .description(description)
            .color(embedColor)
            .fields(fields)
            .image(image)
            .build());

    if (photosSize > 1) {
      List<TweetImage> tweetImages =
          photos.subList(1, photosSize).stream()
              .map(
                  photo -> {
                    TweetImage.TweetImageBuilder builder =
                        TweetImage.builder()
                            .image(photo.getStatical())
                            .userName(tweet.getUser().getLogin())
                            .userId(tweet.getUser().getId())
                            .tweetType(tweet.getType());
                    if (tweet.getRetweeted() != null) {
                      builder.retweetedFrom(tweet.getRetweeted().getUser().getLogin());
                    }

                    return builder.build();
                  })
              .collect(Collectors.toList());

      tweetImages.forEach(
          tweetImage -> {
            Embed imageEmbed = generateTweetImageEmbed(tweetImage, embedColor);

            embeds.add(imageEmbed);
          });
    }

    return embeds;
  }

  public static Embed generateRetweetEmbed(Tweet tweet, int embedColor) {
    String description;
    Image image = null;

    if (!tweet.getText().equals("")) {
      description =
          "**[©"
              + tweet.getUser().getLogin()
              + "]("
              + tweet.getTweetUrl()
              + ")**\n"
              + "> "
              + tweet.getText().replaceAll("\\R", "\n> ");
    } else {
      description = "**[©" + tweet.getUser().getLogin() + "](" + tweet.getTweetUrl() + ")**\n";
    }

    List<Field> fields = new ArrayList<>();

    Author author =
        Author.builder()
            .name(tweet.getUser().getLogin())
            .url(tweet.getTweetUrl())
            .iconUrl(tweet.getUser().getIcon())
            .build();

    Tweet retweeted = tweet.getRetweeted();

    Footer footer =
        Footer.builder()
            .text(
                "RETWEET  —  @"
                    + tweet.getUser().getLogin()
                    + " --> "
                    + "@"
                    + retweeted.getUser().getLogin())
            .build();

    List<Media> photos =
        tweet.getMedia().stream()
            .filter(img -> img.getType().equals(MediaType.PHOTO))
            .collect(Collectors.toList());
    int photosSize = photos.size();

    if (photosSize > 0) {
      image = Image.builder().url(photos.get(0).getStatical()).build();
    }

    Field additionalInfoField =
        Field.builder()
            .inline(false)
            .name("**ADDITIONAL INFO**")
            .value(
                "[**Retweets**]("
                    + tweet.getRetweetsUrl()
                    + ") • [**Following**]("
                    + tweet.getFollowsUrl()
                    + ") • [**Likes**]("
                    + tweet.getLikesUrl()
                    + ")")
            .build();

    Field tweetField =
        Field.builder()
            .inline(false)
            .name("TWEET")
            .value("**[©" + retweeted.getUser().getLogin() + "](" + retweeted.getTweetUrl() + ")**")
            .build();

    fields.add(tweetField);
    fields.add(additionalInfoField);

    return Embed.builder()
        .author(author)
        .footer(footer)
        .title(String.valueOf(tweet.getType()))
        .description(description)
        .color(embedColor)
        .fields(fields)
        .image(image)
        .build();
  }

  public static Embed generateReplyEmbed(Tweet tweet, int embedColor) {
    String description =
        "**[©"
            + tweet.getUser().getLogin()
            + "]("
            + tweet.getTweetUrl()
            + ")**\n"
            + "> "
            + tweet.getText().replaceAll("\\R", "\n> ");
    List<Field> fields = new ArrayList<>();
    Image image = null;

    Author author =
        Author.builder()
            .name(tweet.getUser().getLogin())
            .url(tweet.getTweetUrl())
            .iconUrl(tweet.getUser().getIcon())
            .build();

    Footer footer =
        Footer.builder()
            .text(
                "REPLY  —  @"
                    + tweet.getUser().getLogin()
                    + " --> "
                    + "@"
                    + tweet.getReplied().getUser().getLogin())
            .build();

    List<Media> photos =
        tweet.getMedia().stream()
            .filter(img -> img.getType().equals(MediaType.PHOTO))
            .collect(Collectors.toList());
    int photosSize = photos.size();

    if (photosSize > 0) {
      image = Image.builder().url(photos.get(0).getStatical()).build();
    }

    Field additionalInfoField =
        Field.builder()
            .inline(false)
            .name("**ADDITIONAL INFO**")
            .value(
                "[**Retweets**]("
                    + tweet.getRetweetsUrl()
                    + ") • [**Following**]("
                    + tweet.getFollowsUrl()
                    + ") • [**Likes**]("
                    + tweet.getLikesUrl()
                    + ")")
            .build();

    Field tweetField =
        Field.builder()
            .inline(false)
            .name("TWEET")
            .value(
                "**[©"
                    + tweet.getReplied().getUser().getLogin()
                    + "]("
                    + tweet.getReplied().getTweetUrl()
                    + ")**")
            .build();

    fields.add(tweetField);
    fields.add(additionalInfoField);

    return Embed.builder()
        .author(author)
        .footer(footer)
        .title(String.valueOf(tweet.getType()))
        .description(description)
        .color(embedColor)
        .fields(fields)
        .image(image)
        .build();
  }

  public static List<Embed> generateTweetFromRetweetEmbed(
      Tweet tweet, String retweetedBy, int embedColor) {
    List<Field> fields = new ArrayList<>();
    List<Embed> embeds = new LinkedList<>();

    String description;
    String title = "TWEET";
    Field repliedTweetField = null;

    if (tweet.getType() == TweetType.REPLY) {
      title = "REPLY";

      repliedTweetField =
          Field.builder()
              .inline(false)
              .name("TWEET")
              .value(
                  "**[©"
                      + tweet.getReplied().getUser().getLogin()
                      + "]("
                      + tweet.getReplied().getTweetUrl()
                      + ")**")
              .build();
    }

    if (!tweet.getText().equals("")) {
      description =
          "**[©"
              + tweet.getUser().getLogin()
              + "]("
              + tweet.getTweetUrl()
              + ")**\n"
              + "> "
              + tweet.getText().replaceAll("\\R", "\n> ");
    } else {
      description = "**[©" + tweet.getUser().getLogin() + "](" + tweet.getTweetUrl() + ")**\n";
    }

    Image image = null;

    Author author =
        Author.builder()
            .name(tweet.getUser().getLogin())
            .url(tweet.getTweetUrl())
            .iconUrl(tweet.getUser().getIcon())
            .build();

    Footer footer =
        Footer.builder()
            .text("RETWEET  —  @" + retweetedBy + " --> " + "@" + tweet.getUser().getLogin())
            .build();

    List<Media> photos =
        tweet.getMedia().stream()
            .filter(img -> img.getType().equals(MediaType.PHOTO))
            .collect(Collectors.toList());
    int photosSize = photos.size();

    if (photosSize > 0) {
      image = Image.builder().url(photos.get(0).getStatical()).build();
    }

    Field additionalInfoField =
        Field.builder()
            .inline(false)
            .name("**ADDITIONAL INFO**")
            .value(
                "[**Retweets**]("
                    + tweet.getRetweetsUrl()
                    + ") • [**Following**]("
                    + tweet.getFollowsUrl()
                    + ") • [**Likes**]("
                    + tweet.getLikesUrl()
                    + ")")
            .build();

    if (repliedTweetField != null) {
      fields.add(repliedTweetField);
    }

    fields.add(additionalInfoField);

    embeds.add(
        Embed.builder()
            .author(author)
            .footer(footer)
            .title(title)
            .description(description)
            .color(embedColor)
            .fields(fields)
            .image(image)
            .build());

    if (photosSize > 1) {
      List<TweetImage> tweetImages =
          photos.subList(1, photosSize).stream()
              .map(
                  photo -> {
                    TweetImage.TweetImageBuilder builder =
                        TweetImage.builder()
                            .image(photo.getStatical())
                            .userName(tweet.getUser().getLogin())
                            .userId(tweet.getUser().getId())
                            .tweetType(tweet.getType());

                    if (tweet.getRetweeted() != null) {
                      builder.retweetedFrom(tweet.getRetweeted().getUser().getLogin());
                    }

                    return builder.build();
                  })
              .collect(Collectors.toList());

      tweetImages.forEach(
          tweetImage -> {
            Embed imageEmbed = generateTweetImageEmbed(tweetImage, embedColor);

            embeds.add(imageEmbed);
          });
    }

    return embeds;
  }

  public static Embed generateTweetImageEmbed(TweetImage tweetImage, int embedColor) {

    Footer footer =
        Footer.builder().text("image from TWEET  —  @" + tweetImage.getUserName()).build();

    Image image = Image.builder().url(tweetImage.getImage()).build();

    return Embed.builder().footer(footer).image(image).color(embedColor).build();
  }

  public static Embed generateRetweetImageEmbed(TweetImage tweetImage, int embedColor) {
    Footer footer =
        Footer.builder()
            .text(
                "image from RETWEET  —  @"
                    + tweetImage.getUserName()
                    + " --> "
                    + "@"
                    + tweetImage.getRetweetedFrom())
            .build();

    Image image = Image.builder().url(tweetImage.getImage()).build();

    return Embed.builder().footer(footer).image(image).color(embedColor).build();
  }

  public static Embed generateAlertEmbed(Alert alert, int embedColor) {
    Footer footer = Footer.builder().text("Enigma Robotics").build();

    Field validMonitorsField =
        Field.builder()
            .inline(false)
            .name("**Valid monitors count:**")
            .value(alert.getValidMonitorsCount().toString())
            .build();

    Field failedMonitorsField =
        Field.builder()
            .inline(false)
            .name("**Failed monitors count:**")
            .value(alert.getFailedMonitorsCount().toString())
            .build();

    Field newFailedMonitorsField =
        Field.builder()
            .inline(false)
            .name("**Found the new failed monitor:**")
            .value(alert.getFailedMonitorId())
            .build();

    Field failedReasonField =
        Field.builder().inline(false).name("**Reason:**").value(alert.getReason()).build();

    List<Field> fields = new ArrayList<>();

    fields.add(validMonitorsField);
    fields.add(failedMonitorsField);
    fields.add(newFailedMonitorsField);
    fields.add(failedReasonField);

    return Embed.builder()
        .footer(footer)
        .title("Alert System")
        .color(embedColor)
        .fields(fields)
        .build();
  }

  public static Embed generateTweetRecognitionEmbed(Recognition recognition, int embedColor) {

    DiscordBroadcastRecognitionType discordBroadcastRecognitionType =
        convertRecognitionType(recognition.getType());

    Footer footer = discordBroadcastRecognitionType.generateTweetRecognitionFooter(recognition);
    String title = discordBroadcastRecognitionType.getTitle();

    return Embed.builder()
        .title(title)
        .footer(footer)
        .description(recognition.getResult())
        .color(embedColor)
        .build();
  }

  public static Embed generateRetweetRecognitionEmbed(Recognition recognition, int embedColor) {

    DiscordBroadcastRecognitionType discordBroadcastRecognitionType =
        convertRecognitionType(recognition.getType());

    Footer footer = discordBroadcastRecognitionType.generateRetweetRecognitionFooter(recognition);
    String title = discordBroadcastRecognitionType.getTitle();

    return Embed.builder()
        .title(title)
        .footer(footer)
        .description(recognition.getResult())
        .color(embedColor)
        .build();
  }

  public static Embed generateReplyRecognitionEmbed(Recognition recognition, int embedColor) {

    DiscordBroadcastRecognitionType discordBroadcastRecognitionType =
        convertRecognitionType(recognition.getType());

    Footer footer = discordBroadcastRecognitionType.generateReplyRecognitionFooter(recognition);
    String title = discordBroadcastRecognitionType.getTitle();

    return Embed.builder()
        .title(title)
        .footer(footer)
        .description(recognition.getResult())
        .color(embedColor)
        .build();
  }

  public static Embed generateUserUpdatesEmbed(
      String titleName,
      TwitterUser twitterUser,
      String oldInfo,
      String updatedInfo,
      int embedColor) {

    String title = "PROFILE DETAILS UPDATED — " + "`" + titleName + "`";

    Author author =
        Author.builder()
            .iconUrl(twitterUser.getIcon())
            .name(twitterUser.getLogin())
            .url(twitterUser.getUrl())
            .build();

    List<Field> fields = new LinkedList<>();

    Field.FieldBuilder oldInfoField =
        Field.builder().inline(true).name("Old " + titleName.toLowerCase(Locale.ROOT));

    if (oldInfo.isBlank()) {
      oldInfoField.value("");
    } else oldInfoField.value("> " + oldInfo);

    Field.FieldBuilder updatedInfoField =
        Field.builder().inline(true).name("New " + titleName.toLowerCase(Locale.ROOT));

    if (updatedInfo.isBlank()) {
      updatedInfoField.value("");
    } else updatedInfoField.value("> " + updatedInfo);

    fields.add(oldInfoField.build());
    fields.add(updatedInfoField.build());

    Footer footer =
        Footer.builder().text("PROFILE DETAILS UPDATED -> @" + twitterUser.getLogin()).build();

    return Embed.builder()
        .author(author)
        .title(title)
        .fields(fields)
        .footer(footer)
        .color(embedColor)
        .build();
  }

  public static DiscordBroadcastUserUpdateType convertUserUpdateType(UserUpdateType value) {
    return DiscordBroadcastUserUpdateType.values()[value.ordinal()];
  }

  public static DiscordBroadcastRecognitionType convertRecognitionType(RecognitionType value) {
    return DiscordBroadcastRecognitionType.values()[value.ordinal()];
  }

  public static DiscordBroadcastTweetType convertTweetType(TweetType value) {
    return DiscordBroadcastTweetType.values()[value.ordinal()];
  }
}
