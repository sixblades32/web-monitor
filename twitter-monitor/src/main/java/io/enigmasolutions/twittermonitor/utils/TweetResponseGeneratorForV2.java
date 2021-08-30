package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.broadcastmodels.TweetType;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.v2.Tweet;

import java.util.ArrayList;

public class TweetResponseGeneratorForV2 {
    private TweetResponseGeneratorForV2() {
    }

    public static TweetResponse generateV2(ArrayList<Tweet> tweets, ArrayList<User> users, TweetResponse.TweetResponseBuilder tweetResponseBuilder) {
        if (tweets.size() > 1) {
            tweets.forEach(tweet -> {
                ArrayList<Tweet> retweetTweets = new ArrayList<>(tweets);
                ArrayList<User> retweetUsers = new ArrayList<>(users);

                if (tweet.getType() == TweetType.RETWEET) {
                    tweetResponseBuilder.createdAt(tweet.getCreatedAt())
                            .type(tweet.getType())
                            .tweetId(tweet.getTweetId())
                            .text(tweet.getText())
                            .entities(tweet.getEntities())
                            .extendedEntities(tweet.getExtendedEntities())
                            .inReplyToUserId(tweet.getInReplyToUserId())
                            .inReplyToStatusId(tweet.getInReplyToStatusId())
                            .inReplyToScreenName(tweet.getInReplyToScreenName());

                    users.forEach(user -> {
                        if (user.getId().equals(tweet.getUserId())) {
                            retweetUsers.remove(user);

                            tweetResponseBuilder.user(user);
                        }
                    });

                    if (tweet.getIsQuoteStatus()) {
                        retweetTweets.remove(tweet);

                        tweetResponseBuilder.quotedStatus(generateV2(retweetTweets, retweetUsers, TweetResponse.builder()));
                        tweetResponseBuilder.quotedStatusPermalink(tweet.getQuotedStatusPermalink());
                    } else if (tweet.getRetweetedStatusId() != null) {
                        retweetTweets.remove(tweet);

                        tweetResponseBuilder.text("");
                        tweetResponseBuilder.retweetedStatus(generateV2(retweetTweets, retweetUsers, TweetResponse.builder()));
                    }
                }
            });
        } else if (!tweets.isEmpty()) {
            Tweet tweet = tweets.get(0);
            User user = users.get(0);

            tweetResponseBuilder.createdAt(tweet.getCreatedAt())
                    .type(tweet.getType())
                    .tweetId(tweet.getTweetId())
                    .text(tweet.getText())
                    .entities(tweet.getEntities())
                    .extendedEntities(tweet.getExtendedEntities())
                    .inReplyToUserId(tweet.getInReplyToUserId())
                    .inReplyToStatusId(tweet.getInReplyToStatusId())
                    .inReplyToScreenName(tweet.getInReplyToScreenName())
                    .user(user);

            if (tweets.get(0).getType() == TweetType.REPLY) {
                ArrayList<Tweet> repliedTweetArray = new ArrayList<>();
                repliedTweetArray.add(generateRepliedTweet(tweet));
                ArrayList<User> repliedUserArray = new ArrayList<>();
                repliedUserArray.add(generateRepliedUser(tweet));

                tweetResponseBuilder.repliedStatus(generateV2(repliedTweetArray, repliedUserArray, TweetResponse.builder()));
            }
        }

        return tweetResponseBuilder.build();
    }

    private static Tweet generateRepliedTweet(Tweet tweet) {
        return Tweet.builder()
                .tweetId(tweet.getInReplyToStatusId())
                .build();
    }

    private static User generateRepliedUser(Tweet tweet) {
        return User.builder()
                .id(tweet.getInReplyToUserId())
                .screenName(tweet.getInReplyToScreenName())
                .build();
    }
}
