package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    private final static String BROADCAST_HANDLER_PATH = "/api/broadcast/gateway";

    private final WebSocketHandler webSocketHandler;

    @Autowired
    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of(BROADCAST_HANDLER_PATH, webSocketHandler), -1);
    }
}
