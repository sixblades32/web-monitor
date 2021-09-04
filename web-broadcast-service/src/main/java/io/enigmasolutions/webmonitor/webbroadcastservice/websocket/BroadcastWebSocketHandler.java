package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.BroadcastService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
        return resolveUserClaims()
                .map(claims -> claims.get("customer_id").toString())
                .flatMap(customerId -> session.send(
                        broadcastService
                                .broadcast(customerId)
                                .map(session::textMessage))
                );
    }

    private Mono<Claims> resolveUserClaims() {
        return ReactiveSecurityContextHolder.getContext()
                .map(it -> it.getAuthentication().getCredentials())
                .cast(Claims.class);
    }
}
