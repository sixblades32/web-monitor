package io.enigmasolutions.webmonitor.webbroadcastservice.models.broadcast;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Broadcast {

  private String customerId;
  private String data;
}
