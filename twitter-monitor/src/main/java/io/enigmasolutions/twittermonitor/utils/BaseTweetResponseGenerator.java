package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLTweet;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLUser;

public class BaseTweetResponseGenerator {

    private BaseTweetResponseGenerator() {

    }

    public static TweetResponse generate(GraphQLTweet graphQLTweet) {
        return TweetResponse.builder()
                .createdAt(graphQLTweet.getLegacy().getCreatedAt())
                .type(graphQLTweet.getLegacy().getTweetType())
                .tweetId(graphQLTweet.getRestId())
                .text(graphQLTweet.getLegacy().getText())
                .entities(graphQLTweet.getLegacy().getEntities())
                .extendedEntities(graphQLTweet.getLegacy().getExtendedEntities())
                .user(transformGraphQLUserToBase(graphQLTweet.getCore().getUser()))
                // TODO: split to different method
                .retweetedStatus(graphQLTweet.getLegacy().getRetweetedStatus() != null ?
                        generate(graphQLTweet.getLegacy().getRetweetedStatus()) : null)
                .quotedStatus(graphQLTweet.getLegacy().getQuotedStatus() != null ?
                        generate(graphQLTweet.getLegacy().getQuotedStatus()) : null)
//                .repliedStatus(graphQLTweet.getLegacy().getRepliedStatus() != null ?
//                        generate(graphQLTweet.getLegacy().getRepliedStatus()) : null)
                .inReplyToScreenName(graphQLTweet.getLegacy().getInReplyToScreenName())
                .inReplyToStatusId(graphQLTweet.getLegacy().getInReplyToStatusId())
                .inReplyToUserId(graphQLTweet.getLegacy().getInReplyToUserId())
                .tweetUrl(graphQLTweet.getTweetUrl())
                .retweetsUrl(graphQLTweet.getRetweetsUrl())
                .likesUrl(graphQLTweet.getLikesUrl())
                .followsUrl(graphQLTweet.getLikesUrl())
                .build();
    }

    private static User transformGraphQLUserToBase(GraphQLUser user) {
        return User.builder()
                .id(user.getRestId())
                .screenName(user.getLegacy().getScreenName())
                .name(user.getLegacy().getName())
                .userImage(user.getLegacy().getUserImage())
                .userUrl(user.getLegacy().getUserUrl())
                .isProtected(user.getLegacy().getIsProtected())
                .build();
    }
}
