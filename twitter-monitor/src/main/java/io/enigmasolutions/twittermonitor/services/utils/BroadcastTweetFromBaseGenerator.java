package io.enigmasolutions.twittermonitor.services.utils;

import io.enigmasolutions.twittermonitor.models.broadcast.BroadcastTweet;
import io.enigmasolutions.twittermonitor.models.twitter.base.ExtendedEntity;
import io.enigmasolutions.twittermonitor.models.twitter.base.Tweet;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class BroadcastTweetFromBaseGenerator {

    public BroadcastTweet generate(Tweet tweet) {
        if (tweet == null){
            System.out.println(tweet);
        }
        List<String> images = new LinkedList<>();
        String videoUrl = null;
        String image = null;

        if (tweet.getExtendedEntities() != null) {
            image = tweet.getExtendedEntities().getMedia().get(0).getMediaUrl();

            System.out.println(image);

            tweet.getExtendedEntities().getMedia().stream().forEach(media -> {
                images.add(media.getMediaUrl());
                String text = tweet.getText();
                tweet.setText(text.replace(media.getUrl(), ""));
            });

            if (tweet.getExtendedEntities().getMedia().get(0).getVideoInfo() != null) {
                videoUrl = tweet.getExtendedEntities()
                        .getMedia()
                        .get(0)
                        .getVideoInfo()
                        .getVariants()
                        .get(0)
                        .getUrl();
            }
        }

        if (tweet.getEntities() != null) {
            if (tweet.getEntities().getUrls().size() > 0) {
                tweet.getEntities().getUrls().stream().forEach(url -> {
                    String text = tweet.getText();
                    tweet.setText(text.replace(url.getUrl(), url.getExpandedUrl()));
                });
            }
        }

        BroadcastTweet.BroadcastTweetBuilder broadcastTweetBuilder = BroadcastTweet.builder()
                .tweetType(tweet.getTweetType())
                .text(tweet.getText())
                .userName(tweet.getUser().getScreenName())
                .userIcon(tweet.getUser().getUserImage())
                .userUrl(tweet.getUser().getUserUrl())
                .userId(tweet.getUser().getId())
                .tweetUrl(tweet.getTweetUrl())
                .image(image)
                .images(images)
                .media(videoUrl)
                .followsUrl(tweet.getFollowsUrl())
                .likesUrl(tweet.getLikesUrl())
                .retweetsUrl(tweet.getRetweetsUrl());

        if (tweet.getRetweetedStatus() != null) {
            broadcastTweetBuilder.retweeted(generate(tweet.getRetweetedStatus()));
        } else if (tweet.getQuotedStatus() != null) {
            broadcastTweetBuilder.retweeted(generate(tweet.getQuotedStatus()));
        } else if (tweet.getInReplyToScreenName() != null) {
            broadcastTweetBuilder.replied(generate(tweet.getRepliedStatus()));
        }

        return broadcastTweetBuilder.build();

    }
}
