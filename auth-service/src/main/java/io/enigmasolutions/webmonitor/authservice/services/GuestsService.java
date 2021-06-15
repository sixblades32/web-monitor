package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.models.JwtTokenDto;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GuestsService {

    private final JwtTokenProvider jwtTokenProvider;
    private final DiscordValidationService discordValidationService;

    @Autowired
    public GuestsService(JwtTokenProvider jwtTokenProvider, DiscordValidationService discordValidationService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.discordValidationService = discordValidationService;
    }

    /**
     * Method have to invoked only with DefaultOAuth2User.class
     *
     * @return JwtTokenDto
     */
    public JwtTokenDto generateJwtToken() {
        DefaultOAuth2User authentication = getDefaultOAuth2User();
        String discordId = authentication.getName();

        String generatedToken = generateTokenByDiscordId(discordId);

        return JwtTokenDto.builder()
                .jwt(generatedToken)
                .build();
    }

    /**
     * Method have to be invoked only with UsernamePasswordAuthenticationToken.class
     *
     * @return JwtTokenDto
     */
    public JwtTokenDto refreshJwtToken() {
        Claims claims = resolveUsernamePasswordAuthenticationTokenClaims();
        String discordId = claims.getSubject();

        String generatedToken = generateTokenByDiscordId(discordId);

        return JwtTokenDto.builder()
                .jwt(generatedToken)
                .build();
    }

    private String generateTokenByDiscordId(String discordId) {
        validateGuild(discordId);

        return jwtTokenProvider.generateToken(discordId);
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

    private Claims resolveUsernamePasswordAuthenticationTokenClaims() {
        return (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
