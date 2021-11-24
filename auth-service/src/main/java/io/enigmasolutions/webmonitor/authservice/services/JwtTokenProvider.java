package io.enigmasolutions.webmonitor.authservice.services;

import io.enigmasolutions.webmonitor.authservice.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenProvider {

  private final JwtConfig jwtConfig;

  @Autowired
  public JwtTokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String generateToken(String discordId, String customerId) {
    long now = System.currentTimeMillis();

    return Jwts.builder()
        .setSubject(discordId)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000L))
        .claim("customer_id", customerId)
        .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
        .compact();
  }

  public Claims getClaimsFromJWT(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(jwtConfig.getSecret().getBytes())
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException ex) {
      return ex.getClaims();
    }
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser()
          .setSigningKey(jwtConfig.getSecret().getBytes())
          .parseClaimsJws(authToken);

      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    } catch (ExpiredJwtException ex) {
      return true;
    }

    return false;
  }
}
