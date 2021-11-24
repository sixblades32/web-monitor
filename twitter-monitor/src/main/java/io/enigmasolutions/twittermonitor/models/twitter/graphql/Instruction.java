package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import java.util.List;
import lombok.Data;

@Data
public class Instruction {

  private List<Entry> entries;
}
