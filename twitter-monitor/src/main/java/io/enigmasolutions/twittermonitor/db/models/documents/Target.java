package io.enigmasolutions.twittermonitor.db.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "targets")
public class Target {
    @Id
    private String id;
    private String username;
    private String identifier;
}
