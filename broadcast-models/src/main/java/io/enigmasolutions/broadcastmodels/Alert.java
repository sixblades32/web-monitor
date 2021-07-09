package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private String validMonitorsCount;
    private String failedMonitorsCount;
    private String failedMonitorId;
    private String reason;
}
