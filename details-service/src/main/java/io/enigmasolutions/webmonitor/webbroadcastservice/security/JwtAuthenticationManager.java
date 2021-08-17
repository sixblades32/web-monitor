package io.enigmasolutions.webmonitor.webbroadcastservice.security;

import io.enigmasolutions.webmonitor.webbroadcastservice.services.JwtTokenProvider;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(it -> jwtTokenProvider.validateToken((String) it.getCredentials()))
                .onErrorResume(e -> Mono.empty())
                .map(jws -> new UsernamePasswordAuthenticationToken(
                        authentication.getCredentials(),
                        jws.getBody(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                ));
    }
}
