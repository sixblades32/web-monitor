package io.enigmasolutions.webmonitor.webbroadcastservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Message;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Timeline;

public class DataWrapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String wrapData(Timeline timeline, String data, Class<T> tClass)
            throws JsonProcessingException {
        T tweet = objectMapper.readValue(data, tClass);

        Message<T> message = Message.<T>builder()
                .timeline(timeline)
                .data(tweet)
                .build();

        return objectMapper.writeValueAsString(message);
    }
}
