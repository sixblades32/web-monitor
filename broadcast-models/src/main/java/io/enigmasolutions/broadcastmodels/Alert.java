package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

  private Integer validMonitorsCount;
  private Integer failedMonitorsCount;
  private String failedMonitorId;
  private String reason;
}
