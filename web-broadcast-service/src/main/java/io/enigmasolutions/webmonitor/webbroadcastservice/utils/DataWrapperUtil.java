package io.enigmasolutions.webmonitor.webbroadcastservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Message;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;

public class DataWrapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String wrapData(Timeline timeline, long timestamp, String data, Class<T> tClass)
            throws JsonProcessingException {
        T tweet = null;

        if (data != null && tClass != null) {
            tweet = objectMapper.readValue(data, tClass);
        }

        Message<T> message = Message.<T>builder()
                .timeline(timeline)
                .timestamp(String.valueOf(timestamp))
                .data(tweet)
                .build();

        return objectMapper.writeValueAsString(message);
    }
}
