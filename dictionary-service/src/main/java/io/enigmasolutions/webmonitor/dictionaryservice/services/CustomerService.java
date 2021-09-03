package io.enigmasolutions.webmonitor.dictionaryservice.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.dictionarymodels.CustomerTheme;
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

    public Mono<CustomerDiscordGuild> retrieveCustomerGuildDetails(String id) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    CustomerDiscordGuild customerDiscordGuild = CustomerDiscordGuild.builder()
                            .guildId(customer.getDiscordGuild().getGuildId())
                            .moderatorsRoles(customer.getDiscordGuild().getModeratorsRoles())
                            .usersRoles(customer.getDiscordGuild().getUsersRoles())
                            .build();

                    return Mono.just(customerDiscordGuild);
                });
    }

    public Mono<List<CustomerDiscordBroadcast>> retrieveAllWebhooks() {
        return customerRepository.findAll()
                .flatMap(customer -> {
                    CustomerDiscordBroadcast customerDiscordBroadcast = CustomerDiscordBroadcast.builder()
                            .baseWebhooks(customer.getDiscordBroadcast().getBaseWebhooks())
                            .liveWebhooks(customer.getDiscordBroadcast().getLiveWebhooks())
                            .build();

                    return Mono.just(customerDiscordBroadcast);
                })
                .collect(Collectors.toList());
    }

    public Mono<CustomerTheme> retrieveCustomerTheme(String id) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    CustomerTheme customerTheme = CustomerTheme.builder()
                            .hexColor(customer.getTheme().getHexColor())
                            .logoUrl(customer.getTheme().getLogoUrl())
                            .build();

                    return Mono.just(customerTheme);
                });
    }
}
