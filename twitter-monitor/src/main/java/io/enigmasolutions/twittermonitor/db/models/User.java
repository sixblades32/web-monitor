package io.enigmasolutions.twittermonitor.db.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class User {
    @Field("id")
    private String id;
    @Field("screen_name")
    private String screenName;
}
