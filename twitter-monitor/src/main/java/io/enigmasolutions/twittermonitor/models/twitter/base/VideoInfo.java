package io.enigmasolutions.twittermonitor.models.twitter.base;

import lombok.Data;

import java.util.List;

@Data
public class VideoInfo {
    private List<VideoVariant> variants;
}
