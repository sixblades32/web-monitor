package io.enigmarobotics.discordbroadcastservice.concurent;

import io.enigmarobotics.discordbroadcastservice.db.models.Customer;
import lombok.Data;

@Data
public class ConcurrentCustomer {

    private final Customer customer;

    public ConcurrentCustomer(Customer customer) {
        this.customer = customer;
    }

    public synchronized String retrieveCommonWebhook() {
        String url = customer.getDiscordBroadcast().getCommonWebhooks().remove(0);
        customer.getDiscordBroadcast().getCommonWebhooks().add(url);

        return url;
    }

    public synchronized String retrieveAdvancedWebhook() {
        String url = customer.getDiscordBroadcast().getAdvancedWebhooks().remove(0);
        customer.getDiscordBroadcast().getAdvancedWebhooks().add(url);

        return url;
    }
}
