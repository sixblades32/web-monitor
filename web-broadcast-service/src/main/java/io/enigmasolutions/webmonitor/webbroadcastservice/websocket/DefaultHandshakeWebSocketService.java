package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class DefaultHandshakeWebSocketService extends HandshakeWebSocketService {

    private final JwtTokenProvider jwtTokenProvider;

    public DefaultHandshakeWebSocketService(
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
