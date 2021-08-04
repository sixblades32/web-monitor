package io.enigmarobotics.discordbroadcastservice.concurent;

import io.enigmarobotics.discordbroadcastservice.db.models.documents.Customer;
import lombok.Data;

@Data
public class ConcurrentCustomer {

    private final Customer customer;

    public ConcurrentCustomer(Customer customer) {
        this.customer = customer;
    }

    public synchronized String retrieveBaseWebhook() {
        String url = customer.getDiscordBroadcast().getBaseWebhooks().remove(0);
        customer.getDiscordBroadcast().getBaseWebhooks().add(url);

        return url;
    }

    public synchronized String retrieveLiveWebhook() {
        String url = customer.getDiscordBroadcast().getLiveWebhooks().remove(0);
        customer.getDiscordBroadcast().getLiveWebhooks().add(url);

        return url;
    }
}
