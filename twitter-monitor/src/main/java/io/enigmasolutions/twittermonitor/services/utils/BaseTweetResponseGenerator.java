package io.enigmasolutions.twittermonitor.services.utils;

import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphQL.GraphQLTweet;
import org.springframework.stereotype.Component;

@Component
public class BaseTweetResponseGenerator {

    public TweetResponse generate(GraphQLTweet graphQLTweet){

        TweetResponse tweetResponse = TweetResponse.builder()
                .createdAt(graphQLTweet.getLegacy().getCreatedAt())
                .tweetType(graphQLTweet.getLegacy().getTweetType())
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

        return tweetResponse;

    }
}
