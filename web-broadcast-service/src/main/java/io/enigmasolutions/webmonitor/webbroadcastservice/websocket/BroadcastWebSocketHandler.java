package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class BroadcastWebSocketHandler implements WebSocketHandler {

    private final BroadcastService broadcastService;

    public BroadcastWebSocketHandler(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                broadcastService.broadcast()
                        .map(session::textMessage)
        );
    }
}
