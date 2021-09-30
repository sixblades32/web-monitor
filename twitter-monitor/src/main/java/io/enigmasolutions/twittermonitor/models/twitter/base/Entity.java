package io.enigmasolutions.twittermonitor.models.twitter.base;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Entity {

  private List<Url> urls;
}
