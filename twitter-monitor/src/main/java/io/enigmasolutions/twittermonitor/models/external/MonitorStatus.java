package io.enigmasolutions.twittermonitor.models.external;

import io.enigmasolutions.twittermonitor.models.monitor.Status;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitorStatus {

    private Status status;
    private User user;
}
