package io.enigmasolutions.webmonitor.webbroadcastservice.security;

import io.enigmasolutions.webmonitor.webbroadcastservice.configuration.JwtConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtConfig jwtConfig;
    private final JwtAuthenticationManager authenticationManager;

    public JwtServerSecurityContextRepository(JwtConfig jwtConfig, JwtAuthenticationManager authenticationManager) {
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.error(new UnsupportedOperationException("Not supported yet."));
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getQueryParams().getFirst(jwtConfig.getQueryParam()))
                .flatMap(queryParam -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(queryParam, queryParam);
                    return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }
}
