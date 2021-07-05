package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class QueryStringData {
    private String userId;
    private int count;
    private Boolean withHighlightedLabel;
    private Boolean withTweetQuoteCount;
    private Boolean includePromotedContent;
    private Boolean withTweetResult;
    private Boolean withUserResults;
    private Boolean withVoice;
    private Boolean withNonLegacyCard;
    private Boolean withBirdwatchPivots;
}
