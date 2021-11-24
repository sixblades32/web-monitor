package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLTweet;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLUser;

public class TweetResponseGeneratorForGraphQL {

  private TweetResponseGeneratorForGraphQL() {
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
        .retweetedStatus(graphQLTweet.getLegacy().getRetweetedStatus() != null ?
            generate(graphQLTweet.getLegacy().getRetweetedStatus().getResult()) : null)
        .quotedStatus(graphQLTweet.getQuotedStatus() != null ?
            generate(graphQLTweet.getQuotedStatus().getResult()) : null)
        .repliedStatus(graphQLTweet.getLegacy().getRepliedStatus() != null ?
            generateReplied(graphQLTweet.getLegacy().getRepliedStatus()) : null)
        .quotedStatusPermalink(graphQLTweet.getLegacy().getQuotedStatusPermalink())
        .inReplyToScreenName(graphQLTweet.getLegacy().getInReplyToScreenName())
        .inReplyToStatusId(graphQLTweet.getLegacy().getInReplyToStatusId())
        .inReplyToUserId(graphQLTweet.getLegacy().getInReplyToUserId())
        .tweetUrl(graphQLTweet.getTweetUrl())
        .retweetsUrl(graphQLTweet.getRetweetsUrl())
        .likesUrl(graphQLTweet.getLikesUrl())
        .followsUrl(graphQLTweet.getLikesUrl())
        .build();
  }

  private static TweetResponse generateReplied(GraphQLTweet graphQLTweet) {
    return TweetResponse.builder()
        .user(transformGraphQLUserToBase(graphQLTweet.getCore().getUser()))
        .tweetUrl(graphQLTweet.getTweetUrl())
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
