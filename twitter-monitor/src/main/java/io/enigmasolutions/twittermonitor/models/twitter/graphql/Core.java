package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Core {

  public GraphQLUser user;
}
