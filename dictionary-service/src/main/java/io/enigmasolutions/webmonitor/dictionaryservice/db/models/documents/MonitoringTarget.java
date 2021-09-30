package io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents;

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
@Document(collection = "monitoring-targets")
public class MonitoringTarget {

  @Id
  private String id;
  private String username;
  private String identifier;
  private String image;
  private String name;
}
