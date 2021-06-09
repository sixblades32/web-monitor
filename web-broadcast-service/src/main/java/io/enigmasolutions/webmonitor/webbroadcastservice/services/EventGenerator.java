package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmasolutions.webmonitor.webbroadcastservice.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EventGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final EventUnicastService eventUnicastService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventGenerator(EventUnicastService eventUnicastService, ObjectMapper objectMapper) {
        this.eventUnicastService = eventUnicastService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void generateEvent() {
        int count = counter.getAndIncrement();
        Event event = Event.builder()
                .name("event")
                .count(count)
                .build();

        try {
            eventUnicastService.tryEmitNext(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
