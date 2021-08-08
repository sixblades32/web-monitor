package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.concurent.ConcurrentCustomer;
import io.enigmarobotics.discordbroadcastservice.db.models.documents.Customer;
import io.enigmarobotics.discordbroadcastservice.db.repositories.CustomerRepository;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostmanService {

    @Value("${alert.discord.url}")
    private String alertDiscordUrl;

    private final DiscordClient discordClient;
    private final CustomerRepository CustomerRepository;
    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

    private List<ConcurrentCustomer> customers;

    @Autowired
    public PostmanService(
            DiscordClient discordClient,
            CustomerRepository CustomerRepository
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

        log.info("Alert embed sent to alert webhook.");
    }


    public void processBase(Message message) {

        customers.forEach(customer -> PROCESSING_EXECUTOR.execute(() -> {
            String url = customer.retrieveBaseWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") common webhook.");
        }));
    }

    public void processLive(Message message) {

        customers.forEach(customer -> PROCESSING_EXECUTOR.execute(() -> {
            String url = customer.retrieveLiveWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") live release webhook.");
        }));
    }

    public void sendRecognition(Message message) {

        customers.forEach(customer -> PROCESSING_EXECUTOR.execute(() -> {
            String url = customer.retrieveBaseWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Recognition embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") live release webhook.");
        }));
    }
}