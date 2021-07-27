package io.enigmasolutions.webmonitor.authservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BaseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
