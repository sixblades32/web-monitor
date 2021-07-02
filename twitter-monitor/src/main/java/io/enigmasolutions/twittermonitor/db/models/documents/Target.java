package io.enigmasolutions.twittermonitor.db.models.documents;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Jacksonized
@Document(collection = "targets")
public class Target {
    @Id
    private String id;
    private String username;
    private String identifier;
}
