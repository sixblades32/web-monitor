package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Timeline {

  @JsonProperty("timeline")
  private NestedTimeline nestedTimeline;
}
