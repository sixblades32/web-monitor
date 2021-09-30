package io.enigmasolutions.twittermonitor.db.models.documents;

import io.enigmasolutions.twittermonitor.db.models.references.Credentials;
import io.enigmasolutions.twittermonitor.db.models.references.Proxy;
import io.enigmasolutions.twittermonitor.db.models.references.TweetDeckAuth;
import io.enigmasolutions.twittermonitor.db.models.references.TwitterUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "twitter-scrapers")
public class TwitterScraper {

  @Id
  private String id;
  private Credentials credentials;
  private TwitterUser twitterUser;
  private TweetDeckAuth tweetDeckAuth;
  private Proxy proxy;
}
