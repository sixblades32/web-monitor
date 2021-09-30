package io.enigmasolutions.twittermonitor.models.twitter.base;

import java.util.List;
import lombok.Data;

@Data
public class VideoInfo {

  private List<VideoVariant> variants;
}
