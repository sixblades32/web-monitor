package io.enigmasolutions.twittermonitor.models.twitter.common;

import java.util.LinkedList;
import lombok.Data;

@Data
public class FollowsList {

  private LinkedList<String> ids;
}
