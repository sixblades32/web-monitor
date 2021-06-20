package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class BroadcastService {

    private final Sinks.Many<String> sink;

    public BroadcastService(
            @Value("${broadcast.history-limit}") Integer historyLimit
    ) {
        this.sink = Sinks.many().replay().limit(historyLimit);
    }

    public void tryEmitNext(String data) {
        sink.tryEmitNext(data);
    }

    public Flux<String> broadcast() {
        return sink.asFlux();
    }
}
