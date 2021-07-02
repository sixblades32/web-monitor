package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphQL.GraphQLTweet;

public class BaseTweetResponseGenerator {

    private BaseTweetResponseGenerator() {

    }

    // TODO: здесь по-моему может быть рекурсия?
    public static TweetResponse generate(GraphQLTweet graphQLTweet) {
        return TweetResponse.builder()
                .createdAt(graphQLTweet.getLegacy().getCreatedAt())
                .type(graphQLTweet.getLegacy().getTweetType())
                .tweetId(graphQLTweet.getRestId())
                .text(graphQLTweet.getLegacy().getText())
                .entities(graphQLTweet.getLegacy().getEntities())
                .extendedEntities(graphQLTweet.getLegacy().getExtendedEntities())
                .user(graphQLTweet.getCore().getUser())
                .retweetedStatus(generate(graphQLTweet.getLegacy().getRetweetedStatus()))
                .quotedStatus(generate(graphQLTweet.getLegacy().getQuotedStatus()))
                .repliedStatus(generate(graphQLTweet.getLegacy().getRepliedStatus()))
                .inReplyToScreenName(graphQLTweet.getLegacy().getInReplyToScreenName())
                .inReplyToStatusId(graphQLTweet.getLegacy().getInReplyToStatusId())
                .inReplyToUserId(graphQLTweet.getLegacy().getInReplyToUserId())
                .tweetUrl(graphQLTweet.getTweetUrl())
                .retweetsUrl(graphQLTweet.getRetweetsUrl())
                .likesUrl(graphQLTweet.getLikesUrl())
                .followsUrl(graphQLTweet.getLikesUrl())
                .build();
    }
}
