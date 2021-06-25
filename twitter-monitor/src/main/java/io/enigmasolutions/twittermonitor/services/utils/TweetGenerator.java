package io.enigmasolutions.twittermonitor.services.utils;

import io.enigmasolutions.broadcastmodels.Tweet;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class TweetGenerator {

    public Tweet generate(TweetResponse tweetResponse) {

        List<String> images = new LinkedList<>();
        String videoUrl = null;
        String image = null;

        if (tweetResponse.getExtendedEntities() != null) {
            retrieveImages(tweetResponse, images);
            if (!images.isEmpty()){
                image = images.get(0);
            }
            videoUrl = retrieveVideo(tweetResponse);
        }

        replaceUrls(tweetResponse);

        Tweet.TweetBuilder tweetBuilder = tuneTweetBuilder(tweetResponse, image, images, videoUrl);

        return tweetBuilder.build();

    }
    public Tweet.TweetBuilder tuneTweetBuilder(TweetResponse tweetResponse, String image, List<String> images, String videoUrl){
        Tweet.TweetBuilder tweetBuilder = Tweet.builder()
                .tweetType(tweetResponse.getTweetType())
                .text(tweetResponse.getText())
                .userName(tweetResponse.getUser().getScreenName())
                .userIcon(tweetResponse.getUser().getUserImage())
                .userUrl(tweetResponse.getUser().getUserUrl())
                .userId(tweetResponse.getUser().getId())
                .tweetUrl(tweetResponse.getTweetUrl())
                .image(image)
                .images(images)
                .media(videoUrl)
                .followsUrl(tweetResponse.getFollowsUrl())
                .likesUrl(tweetResponse.getLikesUrl())
                .retweetsUrl(tweetResponse.getRetweetsUrl());

        if (tweetResponse.getRetweetedStatus() != null) {
            tweetBuilder.retweeted(generate(tweetResponse.getRetweetedStatus()));
        } else if (tweetResponse.getQuotedStatus() != null) {
            tweetBuilder.retweeted(generate(tweetResponse.getQuotedStatus()));
        } else if (tweetResponse.getInReplyToScreenName() != null) {
            tweetBuilder.replied(generate(tweetResponse.getRepliedStatus()));
        }

        return tweetBuilder;
    }

    public void retrieveImages(TweetResponse tweetResponse, List<String> images) {

        tweetResponse.getExtendedEntities()
                .getMedia()
                .forEach(media -> {
                    if(media.getSourceUserId() == null){
                        images.add(media.getMediaUrl());
                        String text = tweetResponse.getText();
                        tweetResponse.setText(text.replace(media.getUrl(), ""));
                    }
                });
    }

    public String retrieveVideo(TweetResponse tweetResponse) {

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

    public void replaceUrls(TweetResponse tweetResponse) {
        if (tweetResponse.getEntities() != null && tweetResponse.getEntities().getUrls().size() > 0) {

            tweetResponse.getEntities()
                    .getUrls()
                    .forEach(url -> {
                        String text = tweetResponse.getText();
                        tweetResponse.setText(text.replace(url.getUrl(), url.getExpandedUrl()));
                    });
        }
    }
}
