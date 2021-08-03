package io.enigmasolutions.webmonitor.authservice.db.models.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "jwt-tokens")
public class JwtToken {
    @Id
    private String id;
    @Indexed(unique = true)
    private String value;
    @Version
    private Long version;
}
