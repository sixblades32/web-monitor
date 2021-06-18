package io.enigmasolutions.twittermonitor.models.twitter;

import lombok.Data;

import java.util.LinkedList;

@Data
public class FollowsList {
    private LinkedList<String> ids;
}
