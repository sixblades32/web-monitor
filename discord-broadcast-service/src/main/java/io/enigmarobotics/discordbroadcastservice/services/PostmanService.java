package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.concurent.ConcurrentCustomer;
import io.enigmarobotics.discordbroadcastservice.db.models.Customer;
import io.enigmarobotics.discordbroadcastservice.db.models.Target;
import io.enigmarobotics.discordbroadcastservice.db.repositories.CustomerRepository;
import io.enigmarobotics.discordbroadcastservice.db.repositories.TargetRepository;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostmanService {

    @Value("${alert.discord.url}")
    private String alertDiscordUrl;

    private final DiscordClient discordClient;
    private final CustomerRepository CustomerRepository;

    private List<ConcurrentCustomer> customers;

    @Autowired
    public PostmanService(
            DiscordClient discordClient,
            CustomerRepository CustomerRepository,
            TargetRepository TargetRepository
    ) {
        this.discordClient = discordClient;
        this.CustomerRepository = CustomerRepository;
    }

    @PostConstruct
    public void init() {
        initCustomers();
    }

    private void initCustomers() {
        List<Customer> customers = CustomerRepository.findAll();
        this.customers = customers.stream()
                .map(ConcurrentCustomer::new)
                .collect(Collectors.toList());
    }

    public void sendAlertEmbed(Message message) {
        discordClient.sendEmbed(alertDiscordUrl, message);
    }


    public void processCommon(Message message) {

        customers.stream().parallel().forEach(customer -> {
            String url = customer.retrieveCommonWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") common webhook.");
        });
    }

    public void processAdvanced(Message message) {

        customers.stream().parallel().forEach(customer -> {
            String url = customer.retrieveAdvancedWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") live release webhook.");
        });
    }

    public void sendRecognition(Message message) {
        customers.stream().parallel().forEach(customer -> {
            String url = customer.retrieveCommonWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Recognition embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") live release webhook.");
        });
    }
}
