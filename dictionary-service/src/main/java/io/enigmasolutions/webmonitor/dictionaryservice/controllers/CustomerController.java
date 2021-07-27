package io.enigmasolutions.webmonitor.dictionaryservice.controllers;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordBroadcast;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordGuild;
import io.enigmasolutions.webmonitor.dictionaryservice.services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<DiscordGuild> getCustomerGuildDetails(@PathVariable String id) {
        return customerService.retrieveCustomerGuildDetails(id);
    }

    @GetMapping("/all/webhooks")
    public Mono<List<DiscordBroadcast>> getAllWebhooks() {
        return customerService.retrieveAllWebhooks();
    }
}
