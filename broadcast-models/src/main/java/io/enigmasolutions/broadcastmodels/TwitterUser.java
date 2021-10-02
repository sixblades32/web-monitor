package io.enigmasolutions.broadcastmodels;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwitterUser {

  private List<UserUpdateType> updateTypes;
  private String name;
  private String icon;
  private String login;
  private String url;
  private String id;
  private String location;
  private String description;
  private String statusUrl;
}
