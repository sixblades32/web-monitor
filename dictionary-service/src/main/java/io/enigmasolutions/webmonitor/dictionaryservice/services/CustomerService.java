package io.enigmasolutions.webmonitor.dictionaryservice.services;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordBroadcast;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordGuild;
import io.enigmasolutions.webmonitor.dictionaryservice.db.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<DiscordGuild> retrieveCustomerGuildDetails(String id) {
        return customerRepository.findById(id)
                .flatMap(customer -> Mono.just(customer.getDiscordGuild()));
    }

    public Mono<List<DiscordBroadcast>> retrieveAllWebhooks() {
        return customerRepository.findAll()
                .flatMap(customer -> Mono.just(customer.getDiscordBroadcast()))
                .collect(Collectors.toList());
    }
}
