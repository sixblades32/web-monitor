package io.enigmasolutions.twittermonitor.db.models.documents;

import io.enigmasolutions.twittermonitor.db.models.references.Credentials;
import io.enigmasolutions.twittermonitor.db.models.references.TweetDeckAuth;
import io.enigmasolutions.twittermonitor.db.models.references.TwitterUser;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "scrapers")
public class TwitterScraper {
    @Id
    private String id;
    private Credentials credentials;
    private TwitterUser twitterUser;
    private TweetDeckAuth tweetDeckAuth;
    private String proxy;
}
