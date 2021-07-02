package io.enigmasolutions.twittermonitor.db.models.documents;

import io.enigmasolutions.twittermonitor.db.models.references.Credentials;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "consumers")
public class TwitterConsumer {
    @Id
    private String id;
    private Credentials credentials;
    private String proxy;
}
