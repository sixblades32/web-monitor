package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import io.enigmasolutions.webmonitor.webbroadcastservice.models.broadcast.Broadcast;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class BroadcastService {

    private final Sinks.Many<Broadcast> sink;

    public BroadcastService() {
        this.sink = Sinks.many().replay().limit(0);
    }

    public void tryEmitNext(Broadcast broadcast) {
        sink.tryEmitNext(broadcast);
    }

    public Flux<String> broadcast(String customerId) {
        return sink.asFlux()
                .flatMap(broadcast -> {
                    if (broadcast.getCustomerId() == null || broadcast.getCustomerId().equals(customerId)) {
                        return Mono.just(broadcast.getData());
                    }
                    return Mono.empty();
                });
    }
}
