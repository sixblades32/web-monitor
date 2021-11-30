package io.enigmasolutions.twittermonitor.models.twitter.common;

import java.util.LinkedList;
import lombok.Data;

@Data
public class FollowingData {

  private LinkedList<String> ids;
}
