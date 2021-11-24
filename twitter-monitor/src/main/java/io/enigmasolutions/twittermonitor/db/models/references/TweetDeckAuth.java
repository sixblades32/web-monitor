package io.enigmasolutions.twittermonitor.db.models.references;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetDeckAuth {

  private String bearer;
  private String authToken;
  private String csrfToken;
}
