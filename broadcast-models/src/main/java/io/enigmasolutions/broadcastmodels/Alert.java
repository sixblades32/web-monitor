package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Alert {
    private Integer validMonitorsCount;
    private Integer failedMonitorsCount;
    private String failedMonitorId;
    private String reason;
}
