package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtHandshakeWebSocketService extends HandshakeWebSocketService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeWebSocketService(
            RequestUpgradeStrategy upgradeStrategy,
            JwtTokenProvider jwtTokenProvider
    ) {
        super(upgradeStrategy);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {
        ServerHttpRequest request = exchange.getRequest();

        String incomingToken = request.getQueryParams().getFirst("token");

        if (!jwtTokenProvider.validateToken(incomingToken)) {
            return Mono
                    .error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
        }

        return super.handleRequest(exchange, handler);
    }
}
