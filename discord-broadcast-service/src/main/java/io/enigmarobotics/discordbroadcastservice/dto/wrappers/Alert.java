package io.enigmarobotics.discordbroadcastservice.dto.wrappers;

import lombok.Data;

@Data
public class Alert {
    private String validMonitorsCount;
    private String failedMonitorsCount;
    private String failedMonitorId;
    private String reason;
}
