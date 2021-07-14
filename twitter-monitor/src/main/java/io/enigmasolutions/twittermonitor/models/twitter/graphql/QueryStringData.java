package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryStringData {
    private String userId;
    private int count;
    private Boolean withHighlightedLabel;
    private Boolean withTweetQuoteCount;
    private Boolean includePromotedContent;
    private Boolean withTweetResult;
    private Boolean withReactions;
    private Boolean withSuperFollowsTweetFields;
    private Boolean withUserResults;
    private Boolean withVoice;
    private Boolean withBirdwatchPivots;
}
