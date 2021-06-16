package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    private final static String BROADCAST_HANDLER_PATH = "/api/broadcast";

    private final WebSocketHandler webSocketHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSocketConfig(WebSocketHandler webSocketHandler, JwtTokenProvider jwtTokenProvider) {
        this.webSocketHandler = webSocketHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of(BROADCAST_HANDLER_PATH, webSocketHandler), -1);
    }

    @Bean
    public HandlerAdapter wsHandlerAdapter() {
        return new WebSocketHandlerAdapter(webSocketService());
    }

    @Bean
    public WebSocketService webSocketService() {
        ReactorNettyRequestUpgradeStrategy strategy = new ReactorNettyRequestUpgradeStrategy();
        return new JwtHandshakeWebSocketService(strategy, jwtTokenProvider);
    }
}
