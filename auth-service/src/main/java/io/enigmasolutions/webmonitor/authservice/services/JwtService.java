package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.dto.JwtTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final DiscordValidationService discordValidationService;

    @Autowired
    public JwtService(JwtTokenProvider jwtTokenProvider, DiscordValidationService discordValidationService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.discordValidationService = discordValidationService;
    }

    public JwtTokenDto generateJwtToken() {
        DefaultOAuth2User defaultOAuth2User = getDefaultOAuth2User();
        String discordId = defaultOAuth2User.getName();

        validateGuild(discordId);

        String generatedToken = jwtTokenProvider.generateToken(discordId);

        return JwtTokenDto.builder()
                .jwt(generatedToken)
                .build();
    }

    public JwtTokenDto refreshJwtToken(String incomingToken) {
        String discordId = jwtTokenProvider.getClaimsFromJWT(incomingToken).getSubject();

        validateGuild(discordId);

        String jwt = jwtTokenProvider.generateToken(discordId);

        return JwtTokenDto.builder()
                .jwt(jwt)
                .build();
    }

    private void validateGuild(String discordId) {
        boolean isUserContainsMutualGuild = discordValidationService.validateMutualGuilds(discordId);
        if (!isUserContainsMutualGuild) {
            throw new AccessDeniedException("Unknown member");
        }
    }

    private DefaultOAuth2User getDefaultOAuth2User() {
        return (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
