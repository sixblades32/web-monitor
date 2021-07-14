package io.enigmasolutions.broadcastmodels;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwitterUser {
    private String name;
    private String icon;
    private String login;
    private String url;
    private String id;
}
