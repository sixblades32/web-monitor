package io.enigmasolutions.webmonitor.webbroadcastservice.services.web;

import io.enigmasolutions.dictionarymodels.CustomerTheme;
import io.enigmasolutions.dictionarymodels.DefaultMonitoringTarget;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DictionaryClient {

  private final WebClient webClient;

  public DictionaryClient(@Value("${dictionary-service.url}") String dictionaryServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(dictionaryServiceUrl)
        .build();
  }

  public Mono<CustomerTheme> getCustomerTheme(String id) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/customers/{id}/theme")
            .build(id))
        .retrieve()
        .bodyToMono(CustomerTheme.class);
  }

  public Mono<List<DefaultMonitoringTarget>> getMonitoringDefaultTargets() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/monitoring/targets/default")
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }
}
