package io.enigmasolutions.dictionarymodels;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDiscordBroadcast {

  private List<String> baseWebhooks;
  private List<String> staffBaseWebhooks;
  private List<String> liveWebhooks;
}

