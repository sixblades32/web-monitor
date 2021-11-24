package io.enigmasolutions.twittermonitor.models.twitter.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class V2Response {

  private GlobalObjects globalObjects;
}
