package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class PostmanService {

    @Value("${alert.discord.url}")
    private String alertDiscordUrl;

    private final DiscordClient discordClient;
    private final DictionaryClientCache dictionaryClientService;
    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();

    @Autowired
    public PostmanService(
            DiscordClient discordClient,
            DictionaryClientCache dictionaryClientService
    ) {
        this.discordClient = discordClient;
        this.dictionaryClientService = dictionaryClientService;
    }

    public void sendAlertEmbed(Message message) {
        discordClient.sendEmbed(alertDiscordUrl, message);

        log.info("Alert embed sent to alert webhook.");
    }


    public void processBase(Message message) {

        dictionaryClientService.getWebhooks().forEach(webhooksPack -> PROCESSING_EXECUTOR.execute(() -> {
            String url = getRandomWebhook(webhooksPack.getBaseWebhooks());

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's" + " base webhook. (" + url + ")");
        }));
    }

    public void processLive(Message message) {

        dictionaryClientService.getWebhooks().forEach(webhooksPack -> PROCESSING_EXECUTOR.execute(() -> {
            String url = getRandomWebhook(webhooksPack.getLiveWebhooks());

            discordClient.sendEmbed(url, message);
            log.info("Tweet embed sent to customer's" + " live release webhook. (" + url + ")");
        }));
    }

    private String getRandomWebhook(List<String> webhooks){
        Random rand = new Random();

        if(webhooks.size() > 0){
            return webhooks.get(rand.nextInt(webhooks.size()));
        }

        return null;
    }
}
