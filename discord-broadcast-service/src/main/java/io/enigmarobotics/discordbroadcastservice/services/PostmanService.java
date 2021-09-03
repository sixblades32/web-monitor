package io.enigmarobotics.discordbroadcastservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmarobotics.discordbroadcastservice.services.web.DiscordClient;
import io.enigmasolutions.broadcastmodels.TweetType;
import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcastConfig;
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

    private final static ExecutorService PROCESSING_EXECUTOR = Executors.newCachedThreadPool();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final DiscordClient discordClient;
    private final DictionaryClientCache dictionaryClientService;
    @Value("${alert.discord.url}")
    private String alertDiscordUrl;

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

        dictionaryClientService.getCustomersConfigs().forEach(customerConfig -> PROCESSING_EXECUTOR.execute(() -> {
            try {
                Message localMessage = objectMapper.readValue(objectMapper.writeValueAsString(message), Message.class);

                String url = getRandomWebhook(customerConfig.getCustomerDiscordBroadcast().getBaseWebhooks());

                if (customerConfig.getTheme().getIsCustom() && localMessage != null) {
                    setColors(localMessage, customerConfig);
                }

                discordClient.sendEmbed(url, localMessage);
                log.info("Tweet embed sent to customer's" + " base webhook. (" + url + ")");
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }));
    }

    public void processLive(Message message) {

        dictionaryClientService.getCustomersConfigs().forEach(customerConfig -> PROCESSING_EXECUTOR.execute(() -> {
            try {
                Message localMessage = objectMapper.readValue(objectMapper.writeValueAsString(message), Message.class);

                String url = getRandomWebhook(customerConfig.getCustomerDiscordBroadcast().getLiveWebhooks());

                if (customerConfig.getTheme().getIsCustom() && localMessage != null) {
                    setColors(localMessage, customerConfig);
                }

                discordClient.sendEmbed(url, localMessage);
                log.info("Tweet embed sent to customer's" + " live release webhook. (" + url + ")");
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }));
    }

    private String getRandomWebhook(List<String> webhooks) {
        Random rand = new Random();

        if (webhooks.size() > 0) {
            return webhooks.get(rand.nextInt(webhooks.size()));
        }

        return null;
    }

    private void setColors(Message message, CustomerDiscordBroadcastConfig customerDiscordBroadcastConfig) {
        Embed mainEmbed = message.getEmbeds().get(0);

        if (mainEmbed.getTitle().equals(String.valueOf(TweetType.TWEET))) {
            message.getEmbeds().forEach(embed -> embed.setColor(customerDiscordBroadcastConfig.getTheme().getTweetColor()));
        } else if (mainEmbed.getTitle().equals(String.valueOf(TweetType.RETWEET))) {
            message.getEmbeds().forEach(embed -> embed.setColor(customerDiscordBroadcastConfig.getTheme().getRetweetColor()));
        } else if (mainEmbed.getTitle().equals(String.valueOf(TweetType.REPLY))) {
            message.getEmbeds().forEach(embed -> embed.setColor(customerDiscordBroadcastConfig.getTheme().getReplyColor()));
        }
    }
}
