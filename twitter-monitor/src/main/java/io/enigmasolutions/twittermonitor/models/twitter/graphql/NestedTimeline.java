package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import java.util.List;
import lombok.Data;

@Data
public class NestedTimeline {

  private List<Instruction> instructions;
}
