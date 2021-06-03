package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.dto.JwtTokenDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JwtTokenDto generateJwtToken() {
        Authentication authentication = getAuthentication();
        String jwt = jwtTokenProvider.generateToken(authentication);

        return JwtTokenDto.builder()
                .jwt(jwt)
                .build();
    }

//    public JwtTokenDto refreshJwtToken(String jwt) {
//        String jwt = jwtTokenProvider.generateToken(authentication);
//
//        return JwtTokenDto.builder()
//                .jwt(jwt)
//                .build();
//    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
