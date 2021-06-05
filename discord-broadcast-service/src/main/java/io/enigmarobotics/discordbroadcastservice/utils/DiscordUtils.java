package io.enigmarobotics.discordbroadcastservice.utils;

import io.enigmarobotics.discordbroadcastservice.dto.models.Author;
import io.enigmarobotics.discordbroadcastservice.dto.models.Embed;
import io.enigmarobotics.discordbroadcastservice.dto.models.Field;
import io.enigmarobotics.discordbroadcastservice.dto.models.Footer;
import io.enigmarobotics.discordbroadcastservice.dto.models.Image;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Alert;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.TweetImage;

import java.util.ArrayList;
import java.util.List;

public class DiscordUtils {

    private DiscordUtils() {

    }

    public static Embed generateTweetEmbed(Tweet tweet, int embedColor) {
        String description = "> " + tweet.getText().replaceAll("\\R", "\n> ");
        List<Field> fields = new ArrayList<>();

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("TWEET — " + tweet.getUserName())
                .build();

        Image image = Image.builder()
                .url(tweet.getImage())
                .build();

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        fields.add(additionalInfoField);

        return Embed.builder()
                .author(author)
                .footer(footer)
                .title(String.valueOf(tweet.getTweetType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();

    }

    public static Embed generateRetweetEmbed(Tweet tweet, int embedColor) {
        String description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n" + "> " +
                tweet.getText().replaceAll("\\R", "\n> ");
        List<Field> fields = new ArrayList<>();

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Tweet retweeted = tweet.getRetweeted();

        Footer footer = Footer.builder()
                .text("RETWEET  —  @" + tweet.getUserName() + " --> " + "@" + retweeted.getUserName())
                .build();

        Image image = Image.builder()
                .url(tweet.getImage())
                .build();

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        Field tweetField = Field.builder()
                .inline(false)
                .name("TWEET")
                .value("**[©" + retweeted.getUserName() + "](" + retweeted.getTweetUrl() + ")**")
                .build();

        fields.add(tweetField);
        fields.add(additionalInfoField);

        return Embed.builder()
                .author(author)
                .footer(footer)
                .title(String.valueOf(tweet.getTweetType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();
    }

    public static Embed generateReplyEmbed(Tweet tweet, int embedColor) {
        String description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n" + "> " +
                tweet.getText().replaceAll("\\R", "\n> ");
        List<Field> fields = new ArrayList<>();

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("REPLY  —  @" + tweet.getUserName() + " --> " + "@" + tweet.getReplied().getUserName())
                .build();

        Image image = Image.builder()
                .url(tweet.getImage())
                .build();

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        Field tweetField = Field.builder()
                .inline(false)
                .name("TWEET")
                .value("**[©" + tweet.getReplied().getUserName() + "](" + tweet.getReplied().getTweetUrl() + ")**")
                .build();

        fields.add(tweetField);
        fields.add(additionalInfoField);

        return Embed.builder()
                .author(author)
                .footer(footer)
                .title(String.valueOf(tweet.getTweetType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();
    }

    public static Embed generateTweetFromRetweetEmbed(Tweet tweet, String retweetedBy, int embedColor) {
        List<Field> fields = new ArrayList<>();

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("RETWEET  —  @" + retweetedBy + " --> " + "@" + tweet.getUserName())
                .build();

        Image image = Image.builder()
                .url(tweet.getImage())
                .build();

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        fields.add(additionalInfoField);

        String description = "> " + tweet.getText().replaceAll("\\R", "\n> ");

        return Embed.builder()
                .author(author)
                .footer(footer)
                .title("TWEET")
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();
    }

    public static Embed generateTweetImageEmbed(TweetImage tweetImage, int embedColor) {

        Footer footer = Footer.builder()
                .text("image from TWEET  —  @" + tweetImage.getUserName())
                .build();

        Image image = Image.builder()
                .url(tweetImage.getImage())
                .build();

       return Embed.builder()
                .footer(footer)
                .image(image)
                .color(embedColor)
                .build();
    }

    public static Embed generateRetweetImageEmbed(TweetImage tweetImage, int embedColor) {

        Footer footer = Footer.builder()
                .text("image from RETWEET  —  @" + tweetImage.getUserName() + " --> " + "@"
                        + tweetImage.getRetweetedFrom())
                .build();

        Image image = Image.builder()
                .url(tweetImage.getImage())
                .build();

        return Embed.builder()
                .footer(footer)
                .image(image)
                .color(embedColor)
                .build();

    }

    public static Embed generateAlertEmbed(Alert alert, int embedColor) {
        Footer footer = Footer.builder()
                .text("Enigma Robotics")
                .build();

        Field validMonitorsField = Field.builder()
                .inline(false)
                .name("**Valid monitors count:**")
                .value(alert.getValidMonitorsCount())
                .build();

        Field failedMonitorsField = Field.builder()
                .inline(false)
                .name("**Failed monitors count:**")
                .value(alert.getFailedMonitorsCount())
                .build();

        Field newFailedMonitorsField = Field.builder()
                .inline(false)
                .name("**Found the new failed monitor:**")
                .value(alert.getFailedMonitorId())
                .build();

        Field failedReasonField = Field.builder()
                .inline(false)
                .name("**Reason:**")
                .value(alert.getReason())
                .build();

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
}
