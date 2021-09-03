package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.configuration.JwtConfig;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.JwtTokenProvider;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtHandshakeWebSocketService extends HandshakeWebSocketService {

    private final JwtConfig jwtConfig;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeWebSocketService(
            RequestUpgradeStrategy upgradeStrategy,
            JwtConfig jwtConfig, JwtTokenProvider jwtTokenProvider
    ) {
        super(upgradeStrategy);
        this.jwtConfig = jwtConfig;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {
        return Mono.just(exchange)
                .mapNotNull(it -> it.getRequest()
                        .getQueryParams()
                        .getFirst(jwtConfig.getQueryParam()))
                .map(jwtTokenProvider::validateToken)
                .onErrorResume(e -> Mono.error(new ResponseStatusException(UNAUTHORIZED)))
                .flatMap(jws -> super.handleRequest(exchange, handler));
    }
}
