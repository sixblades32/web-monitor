package io.enigmasolutions.webmonitor.dictionaryservice.controllers;

import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcastConfig;
import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.dictionarymodels.CustomerTheme;
import io.enigmasolutions.webmonitor.dictionaryservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}/guild")
    public Mono<CustomerDiscordGuild> getCustomerGuildDetails(@PathVariable String id) {
        return customerService.retrieveCustomerGuildDetails(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/guilds/all")
    public Mono<List<CustomerDiscordGuild>> getAllCustomerGuildDetails() {
        return customerService.retrieveAllCustomersGuildDetails();
    }

    @GetMapping("/discord/broadcast/configs/all")
    public Mono<List<CustomerDiscordBroadcastConfig>> getAllCustomersDiscordBroadcastConfigs() {
        return customerService.retrieveAllCustomersDiscordBroadcastConfigs();
    }



    @GetMapping("/{id}/theme")
    public Mono<CustomerTheme> getCustomerTheme(@PathVariable String id) {
        return customerService.retrieveCustomerTheme(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
