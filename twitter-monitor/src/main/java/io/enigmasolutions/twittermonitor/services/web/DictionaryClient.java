package io.enigmasolutions.twittermonitor.services.web;

import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DictionaryClient {

  private final WebClient webClient;

  public DictionaryClient(@Value("${dictionary-service.url}") String dictionaryServiceUrl) {
    this.webClient = WebClient.builder().baseUrl(dictionaryServiceUrl).build();
  }

  public Mono<User> createMonitoringTarget(User user) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/monitoring-targets").build())
        .body(Mono.just(user), User.class)
        .retrieve()
        .bodyToMono(User.class);
  }

  public Mono<User> deleteMonitoringTarget(String id) {
    return webClient
        .delete()
        .uri(uriBuilder -> uriBuilder.path("/monitoring-targets/" + id).build())
        .retrieve()
        .bodyToMono(User.class);
  }
}
