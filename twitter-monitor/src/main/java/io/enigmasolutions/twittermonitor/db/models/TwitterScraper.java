package io.enigmasolutions.twittermonitor.db.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "scrapers")
@Data
public class TwitterScraper {
    @Id
    private String id;
    private Credentials credentials;
    private User user;
    @Field("td_auth")
    private TweetDeckAuth tweetDeckAuth;
    private String proxy;
}
