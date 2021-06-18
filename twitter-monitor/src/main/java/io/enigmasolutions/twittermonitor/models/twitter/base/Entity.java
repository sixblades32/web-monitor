package io.enigmasolutions.twittermonitor.models.twitter.base;

import lombok.Data;

import java.util.List;

@Data
public class Entity {
    private List<Url> urls;
}
