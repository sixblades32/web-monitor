package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Webhook {
    private String url;
    private String type;
}
