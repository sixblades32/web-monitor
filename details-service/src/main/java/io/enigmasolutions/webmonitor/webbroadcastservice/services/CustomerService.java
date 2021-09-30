package io.enigmasolutions.webmonitor.webbroadcastservice.services;

import io.enigmasolutions.dictionarymodels.CustomerTheme;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.web.DictionaryClient;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

  private final DictionaryClient dictionaryClient;

  public CustomerService(DictionaryClient dictionaryClient) {
    this.dictionaryClient = dictionaryClient;
  }

  public Mono<CustomerTheme> retrieveCustomerTheme() {
    return resolveUserClaims()
        .map(claims -> claims.get("customer_id").toString())
        .flatMap(dictionaryClient::getCustomerTheme);
  }

  private Mono<Claims> resolveUserClaims() {
    return ReactiveSecurityContextHolder.getContext()
        .map(it -> it.getAuthentication().getCredentials())
        .cast(Claims.class);
  }
}
