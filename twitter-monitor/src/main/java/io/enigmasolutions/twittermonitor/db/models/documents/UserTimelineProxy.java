package io.enigmasolutions.twittermonitor.db.models.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user-timeline-proxy")
public class UserTimelineProxy {
    private String host;
    private String port;
    private String login;
    private String password;
}
