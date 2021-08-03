package io.enigmasolutions.webmonitor.authservice.security;

import io.enigmasolutions.webmonitor.authservice.configuration.JwtConfig;
import io.enigmasolutions.webmonitor.authservice.services.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtConfig jwtConfig, JwtTokenProvider jwtTokenProvider) {
        this.jwtConfig = jwtConfig;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(jwtConfig.getHeader());

        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String incomingToken = header.replace(jwtConfig.getPrefix(), "").trim();

        if (!jwtTokenProvider.validateToken(incomingToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtTokenProvider.getClaimsFromJWT(incomingToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                incomingToken,
                claims,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
