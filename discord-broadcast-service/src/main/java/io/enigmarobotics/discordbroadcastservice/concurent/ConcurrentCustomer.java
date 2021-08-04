package io.enigmarobotics.discordbroadcastservice.concurent;

import io.enigmarobotics.discordbroadcastservice.db.models.documents.Customer;
import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import lombok.Data;

@Data
public class ConcurrentCustomer {

    private final CustomerDiscordBroadcast customer;

    public ConcurrentCustomer(CustomerDiscordBroadcast customer) {
        this.customer = customer;
    }

    public synchronized String retrieveBaseWebhook() {
        String url = customer.getBaseWebhooks().remove(0);
        customer.getBaseWebhooks().add(url);

        return url;
    }

    public synchronized String retrieveLiveWebhook() {
        String url = customer.getLiveWebhooks().remove(0);
        customer.getLiveWebhooks().add(url);

        return url;
    }
}
