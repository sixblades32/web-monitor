package io.enigmarobotics.discordbroadcastservice.utils;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.*;
import io.enigmarobotics.discordbroadcastservice.domain.wrappers.*;
import io.enigmasolutions.broadcastmodels.Recognition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordUtils {

    private DiscordUtils() {

    }

    public static List<Embed> generateTweetEmbed(BroadcastTweet tweet, int embedColor) {
        String description = "> " + tweet.getText().replaceAll("\\R", "\n> ");
        List<Field> fields = new ArrayList<>();
        List<Embed> embeds = new LinkedList<>();
        Image image = null;

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("TWEET — " + tweet.getUserName())
                .build();
        if (tweet.getImages().size() > 0){
            image = Image.builder()
                    .url(tweet.getImages().get(0))
                    .build();
        }

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        fields.add(additionalInfoField);

        embeds.add(Embed.builder()
                .author(author)
                .footer(footer)
                .title(String.valueOf(tweet.getType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build());

        if (tweet.getImages().size() > 1) {
            List<String> images = tweet.getImages();
            List<TweetImage> tweetImages = images.subList(1, images.size()).stream().map(url -> {
                TweetImage.TweetImageBuilder builder = TweetImage.builder()
                        .image(url)
                        .userName(tweet.getUserName())
                        .userId(tweet.getUserId())
                        .tweetType(tweet.getType());

                if (tweet.getRetweeted() != null) {
                    builder.retweetedFrom(tweet.getRetweeted().getUserName());
                }

                return builder.build();
            }).collect(Collectors.toList());

            tweetImages.forEach(tweetImage -> {
                Embed imageEmbed = generateTweetImageEmbed(tweetImage, embedColor);

                embeds.add(imageEmbed);
            });
        }

        return embeds;

    }

    public static Embed generateRetweetEmbed(BroadcastTweet tweet, int embedColor) {
        String description = "";
        Image image = null;
        if(tweet.getText() != ""){
            description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n" + "> " +
                    tweet.getText().replaceAll("\\R", "\n> ");
        }else {
            description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n";
        }

        List<Field> fields = new ArrayList<>();

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        BroadcastTweet retweeted = tweet.getRetweeted();

        Footer footer = Footer.builder()
                .text("RETWEET  —  @" + tweet.getUserName() + " --> " + "@" + retweeted.getUserName())
                .build();

        if (tweet.getImages().size() > 0){
            image = Image.builder()
                    .url(tweet.getImages().get(0))
                    .build();
        }

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
                .title(String.valueOf(tweet.getType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();
    }

    public static Embed generateReplyEmbed(BroadcastTweet tweet, int embedColor) {
        String description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n" + "> " +
                tweet.getText().replaceAll("\\R", "\n> ");
        List<Field> fields = new ArrayList<>();
        Image image = null;

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("REPLY  —  @" + tweet.getUserName() + " --> " + "@" + tweet.getReplied().getUserName())
                .build();

        if (tweet.getImages().size() > 0){
            image = Image.builder()
                    .url(tweet.getImages().get(0))
                    .build();
        }

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
                .title(String.valueOf(tweet.getType()))
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build();
    }

    public static List<Embed> generateTweetFromRetweetEmbed(BroadcastTweet tweet, String retweetedBy, int embedColor) {
        List<Field> fields = new ArrayList<>();
        List<Embed> embeds = new LinkedList<>();

        String description = "";

        if(tweet.getText() != ""){
            description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n" + "> " +
                    tweet.getText().replaceAll("\\R", "\n> ");
        }else {
            description = "**[©" + tweet.getUserName() + "](" + tweet.getTweetUrl() + ")**\n";
        }

        Image image = null;

        Author author = Author.builder()
                .name(tweet.getUserName())
                .url(tweet.getTweetUrl())
                .iconUrl(tweet.getUserIcon())
                .build();

        Footer footer = Footer.builder()
                .text("RETWEET  —  @" + retweetedBy + " --> " + "@" + tweet.getUserName())
                .build();

        if (tweet.getImages().size() > 0){
            image = Image.builder()
                    .url(tweet.getImages().get(0))
                    .build();
        }

        Field additionalInfoField = Field.builder()
                .inline(false)
                .name("**ADDITIONAL INFO**")
                .value("[**Retweets**](" + tweet.getRetweetsUrl() + ") • [**Following**](" + tweet.getFollowsUrl() +
                        ") • [**Likes**](" + tweet.getLikesUrl() + ")")
                .build();

        fields.add(additionalInfoField);

        embeds.add(Embed.builder()
                .author(author)
                .footer(footer)
                .title("TWEET")
                .description(description)
                .color(embedColor)
                .fields(fields)
                .image(image)
                .build());

        if (tweet.getImages().size() > 1) {
            List<String> images = tweet.getImages();
            List<TweetImage> tweetImages = images.subList(1, images.size()).stream().map(url -> {
                TweetImage.TweetImageBuilder builder = TweetImage.builder()
                        .image(url)
                        .userName(tweet.getUserName())
                        .userId(tweet.getUserId())
                        .tweetType(tweet.getType());

                if (tweet.getRetweeted() != null) {
                    builder.retweetedFrom(tweet.getRetweeted().getUserName());
                }

                return builder.build();
            }).collect(Collectors.toList());

            tweetImages.forEach(tweetImage -> {
                Embed imageEmbed = generateTweetImageEmbed(tweetImage, embedColor);

                embeds.add(imageEmbed);
            });
        }

        return embeds;
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

    public static Embed generatePastebinRecognitionEmbed(BroadcastRecognition recognition, int embedColor) {

        Footer footer = recognition.getRecognitionType().generateTweetRecognitionFooter(recognition);

        return Embed.builder()
                .footer(footer)
                .description(recognition.getResult())
                .color(embedColor)
                .build();
    }
}
