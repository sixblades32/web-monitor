package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.db.models.documents.JwtToken;
import io.enigmasolutions.webmonitor.authservice.db.repositories.JwtTokenRepository;
import io.enigmasolutions.webmonitor.authservice.exceptions.DeprecatedTokenException;
import io.enigmasolutions.webmonitor.authservice.exceptions.NoMutualGuildException;
import io.enigmasolutions.webmonitor.authservice.models.external.JwtTokenDto;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final JwtTokenProvider jwtTokenProvider;
  private final DiscordValidationService discordValidationService;
  private final JwtTokenRepository jwtTokenRepository;

  @Autowired
  public CustomerService(
      JwtTokenProvider jwtTokenProvider,
      DiscordValidationService discordValidationService,
      JwtTokenRepository jwtTokenRepository
  ) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.discordValidationService = discordValidationService;
    this.jwtTokenRepository = jwtTokenRepository;
  }

  /**
   * Method have to be invoked only with UsernamePasswordAuthenticationToken.class
   *
   * @return JwtTokenDto
   */
  public JwtTokenDto refreshJwtToken() {
    UsernamePasswordAuthenticationToken token = resolveUsernamePasswordAuthenticationToken();

    String tokenPrincipal = token.getPrincipal().toString();
    Claims claims = (Claims) token.getCredentials();

    String discordId = claims.getSubject();
    String customerId = claims.get("customer_id").toString();

    JwtToken jwtToken = jwtTokenRepository.findJwtTokenByValue(tokenPrincipal)
        .orElseThrow(() ->
            new DeprecatedTokenException(
                String.format("Deprecated token was rejected for user with Discord id: %s",
                    discordId)
            ));

    if (!discordValidationService.isUserContainsMutualGuild(discordId, customerId)) {
      throw new NoMutualGuildException(
          String.format(
              "Token refresh was rejected for user with Discord id: %s, no mutual guilds",
              discordId
          ));
    }

    String generatedToken = jwtTokenProvider.generateToken(discordId, customerId);

    jwtToken.setValue(generatedToken);
    jwtTokenRepository.save(jwtToken);

    return JwtTokenDto.builder()
        .jwt(generatedToken)
        .build();
  }

  private UsernamePasswordAuthenticationToken resolveUsernamePasswordAuthenticationToken() {
    return (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
  }
}
