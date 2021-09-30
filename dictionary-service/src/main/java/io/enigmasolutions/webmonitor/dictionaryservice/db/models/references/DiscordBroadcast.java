package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordBroadcast {

  private List<String> baseWebhooks;
  private List<String> staffBaseWebhooks;
  private List<String> liveWebhooks;
}

