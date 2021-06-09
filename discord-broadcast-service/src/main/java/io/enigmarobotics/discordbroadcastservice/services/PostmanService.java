package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.concurent.ConcurrentCustomer;
import io.enigmarobotics.discordbroadcastservice.db.models.Customer;
import io.enigmarobotics.discordbroadcastservice.db.models.Target;
import io.enigmarobotics.discordbroadcastservice.db.repositories.CustomerRepository;
import io.enigmarobotics.discordbroadcastservice.db.repositories.TargetRepository;
import io.enigmarobotics.discordbroadcastservice.dto.models.Message;
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
    private final TargetRepository TargetRepository;

    private final List<String> advancedTargetIds = new LinkedList<>();

    private List<String> commonTargetIds;
    private List<ConcurrentCustomer> customers;

    @Autowired
    public PostmanService(
            DiscordClient discordClient,
            CustomerRepository CustomerRepository,
            TargetRepository TargetRepository
    ) {
        this.discordClient = discordClient;
        this.CustomerRepository = CustomerRepository;
        this.TargetRepository = TargetRepository;
    }

    @PostConstruct
    public void init() {
        initCustomers();
        initTargets();
    }

    private void initCustomers() {
        List<Customer> customers = CustomerRepository.findAll();
        this.customers = customers.stream()
                .map(ConcurrentCustomer::new)
                .collect(Collectors.toList());
    }

    private void initTargets() {
        List<Target> targets = TargetRepository.findAll();

        commonTargetIds = targets.stream()
                .map(Target::getIdentifier)
                .collect(Collectors.toList());
    }

    public void sendAlertEmbed(Message message) {
        discordClient.sendEmbed(alertDiscordUrl, message);
    }

    public void sendTwitterEmbed(Message message, String userId) {
        CompletableFuture.runAsync(() -> processCommon(message, userId));
        CompletableFuture.runAsync(() -> processAdvanced(message, userId));
    }

    private void processCommon(Message message, String userId) {
        if (!checkCommonPass(userId)) return;

        customers.stream().parallel().forEach(customer -> {
            String url = customer.retrieveCommonWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") common webhook.");
        });
    }

    private void processAdvanced(Message message, String userId) {
        if (!checkAdvancedPass(userId)) return;

        customers.stream().parallel().forEach(customer -> {
            String url = customer.retrieveAdvancedWebhook();

            discordClient.sendEmbed(url, message);
            log.info("Embed sent to customer's(id: " + customer.getCustomer().getCustomerId() + ") advanced webhook.");
        });
    }

    private Boolean checkCommonPass(String id) {
        return commonTargetIds.contains(id);
    }

    private Boolean checkAdvancedPass(String id) {
        return advancedTargetIds.contains(id);
    }
}
