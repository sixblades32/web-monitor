package io.enigmasolutions.twittermonitor.models.external;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTimelineClusterRestoreBody {

  private TwitterScraper twitterScraper;
  private String screenName;
}
