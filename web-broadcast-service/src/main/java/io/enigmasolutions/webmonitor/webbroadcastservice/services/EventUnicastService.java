package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class EventUnicastService {

    private final Sinks.Many<String> sink;

    public EventUnicastService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        ;
    }

    public void tryEmitNext(String next) {
        sink.tryEmitNext(next);
    }

    public Flux<String> getMessages() {
        return sink.asFlux();
    }
}
