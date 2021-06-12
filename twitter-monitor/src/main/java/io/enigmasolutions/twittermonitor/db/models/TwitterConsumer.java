package io.enigmasolutions.twittermonitor.db.models;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
@Data
public class TwitterConsumer {
    @Id
    private String id;
    private Credentials credentials;
    private String proxy;
}
