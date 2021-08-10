package io.enigmasolutions.twittermonitor.db.models.documents;

import io.enigmasolutions.twittermonitor.db.models.references.Credentials;
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
@Document(collection = "twitter-consumers")
public class TwitterConsumer {
    @Id
    private String id;
    private Credentials credentials;
    private String proxy;
}
