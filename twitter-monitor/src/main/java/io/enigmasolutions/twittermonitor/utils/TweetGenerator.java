package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.broadcastmodels.BriefTweet;
import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;

import java.util.LinkedList;
import java.util.List;

public class TweetGenerator {

    private TweetGenerator() {

    }

    public static Tweet generate(TweetResponse tweetResponse) {
        List<String> images = new LinkedList<>();
        String videoUrl = null;

        if (tweetResponse.getExtendedEntities() != null) {
            retrieveImages(tweetResponse, images);
            videoUrl = retrieveVideo(tweetResponse);
        }

        replaceUrls(tweetResponse);

        return buildTweet(tweetResponse, images, videoUrl);
    }

    private static Tweet buildTweet(TweetResponse tweetResponse, List<String> images, String videoUrl) {
        Tweet.TweetBuilder tweetBuilder = Tweet.builder()
                .type(tweetResponse.getType())
                .text(tweetResponse.getText())
                .user(buildTweetUser(tweetResponse))
                .tweetUrl(tweetResponse.getTweetUrl())
                .images(images)
                .media(videoUrl)
                .followsUrl(tweetResponse.getFollowsUrl())
                .likesUrl(tweetResponse.getLikesUrl())
                .retweetsUrl(tweetResponse.getRetweetsUrl());

        if (tweetResponse.getRetweetedStatus() != null) {
            tweetBuilder.retweeted(generate(tweetResponse.getRetweetedStatus()));
        } else if (tweetResponse.getQuotedStatus() != null) {
            tweetBuilder.retweeted(generate(tweetResponse.getQuotedStatus()));
        } else if (tweetResponse.getInReplyToStatusId() != null) {
            tweetBuilder.replied(buildBriefTweet(tweetResponse.getRepliedStatus()));
        }

        return tweetBuilder.build();
    }

    public static BriefTweet buildBriefTweet(TweetResponse tweetResponse) {
        return BriefTweet.builder()
                .user(buildTweetUser(tweetResponse))
                .tweetUrl(tweetResponse.getTweetUrl())
                .build();
    }

    private static String retrieveVideo(TweetResponse tweetResponse) {
        return tweetResponse.getExtendedEntities()
                .getMedia()
                .get(0)
                .getVideoInfo() != null ? tweetResponse.getExtendedEntities()
                .getMedia()
                .get(0)
                .getVideoInfo()
                .getVariants()
                .get(0)
                .getUrl() : null;
    }

    private static void retrieveImages(TweetResponse tweetResponse, List<String> images) {
        tweetResponse.getExtendedEntities()
                .getMedia()
                .forEach(media -> {
                    if (media.getSourceUserId() == null) {
                        images.add(media.getMediaUrl());
                        String text = tweetResponse.getText();
                        tweetResponse.setText(text.replace(media.getUrl(), ""));
                    }
                });
    }

    private static void replaceUrls(TweetResponse tweetResponse) {
        if (tweetResponse.getEntities() != null && tweetResponse.getEntities().getUrls().size() > 0) {

            tweetResponse.getEntities()
                    .getUrls()
                    .forEach(url -> {
                        String text = tweetResponse.getText();
                        tweetResponse.setText(text.replace(url.getUrl(), url.getExpandedUrl()));
                    });
        }
    }

    private static TwitterUser buildTweetUser(TweetResponse tweetResponse) {
        return TwitterUser.builder()
                .name(tweetResponse.getUser().getName())
                .icon(tweetResponse.getUser().getUserImage())
                .login(tweetResponse.getUser().getScreenName())
                .url(tweetResponse.getUser().getUserUrl())
                .id(tweetResponse.getUser().getId())
                .build();
    }
}
