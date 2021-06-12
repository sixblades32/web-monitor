package io.enigmasolutions.twittermonitor.db.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scrapers")
@Data
public class TwitterScraper {
    @Id
    private String id;
    private Credentials credentials;
    private User user;
    private String proxy;
}
