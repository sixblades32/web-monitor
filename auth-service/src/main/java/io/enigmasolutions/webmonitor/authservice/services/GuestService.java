package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.db.models.documents.JwtToken;
import io.enigmasolutions.webmonitor.authservice.db.repositories.JwtTokenRepository;
import io.enigmasolutions.webmonitor.authservice.exceptions.DeprecatedTokenException;
import io.enigmasolutions.webmonitor.authservice.exceptions.NoMutualGuildException;
import io.enigmasolutions.webmonitor.authservice.models.external.JwtTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

  private final JwtTokenProvider jwtTokenProvider;
  private final DiscordValidationService discordValidationService;
  private final JwtTokenRepository jwtTokenRepository;

  @Autowired
  public GuestService(
      JwtTokenProvider jwtTokenProvider,
      DiscordValidationService discordValidationService,
      JwtTokenRepository jwtTokenRepository
  ) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.discordValidationService = discordValidationService;
    this.jwtTokenRepository = jwtTokenRepository;
  }

  /**
   * Method have to be invoked only with DefaultOAuth2User.class
   *
   * @return JwtTokenDto
   */
  public JwtTokenDto generateJwtToken(String customerId) {
    DefaultOAuth2User authentication = getDefaultOAuth2User();
    String discordId = authentication.getName();

    if (!discordValidationService.isUserContainsMutualGuild(discordId, customerId)) {
      throw new NoMutualGuildException(
          String.format(
              "Token refresh was rejected for user with Discord id: %s, no mutual guilds",
              discordId
          ));
    }

    String generatedToken = jwtTokenProvider.generateToken(discordId, customerId);

    JwtToken jwtToken = JwtToken.builder()
        .value(generatedToken)
        .build();
    try {
      jwtTokenRepository.save(jwtToken);
    } catch (OptimisticLockingFailureException e) {
      throw new DeprecatedTokenException(
          String.format("Deprecated token was rejected for user with Discord id: %s", discordId)
      );
    }

    SecurityContextHolder.clearContext();

    return JwtTokenDto.builder()
        .jwt(generatedToken)
        .build();
  }

  private DefaultOAuth2User getDefaultOAuth2User() {
    return (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
  }
}
