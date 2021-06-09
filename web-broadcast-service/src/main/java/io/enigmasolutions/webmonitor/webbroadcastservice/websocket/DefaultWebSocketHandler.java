package io.enigmasolutions.webmonitor.webbroadcastservice.websocket;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.EventUnicastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DefaultWebSocketHandler implements WebSocketHandler {

    private final EventUnicastService eventUnicastService;

    @Autowired
    public DefaultWebSocketHandler(EventUnicastService eventUnicastService) {
        this.eventUnicastService = eventUnicastService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messages = session.receive()
                .flatMap(message -> eventUnicastService.getMessages())
                .flatMap(Mono::just).map(session::textMessage);

        return session.send(messages);
    }
}
