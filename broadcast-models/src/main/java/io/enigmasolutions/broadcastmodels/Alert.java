package io.enigmasolutions.broadcastmodels;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Alert {
    private Integer validMonitorsCount;
    private Integer failedMonitorsCount;
    private String failedMonitorId;
    private String reason;
}
