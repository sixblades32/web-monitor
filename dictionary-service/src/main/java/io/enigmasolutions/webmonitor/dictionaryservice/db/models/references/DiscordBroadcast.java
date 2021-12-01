package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import java.util.List;
import java.util.stream.Collectors;

import io.enigmasolutions.dictionarymodels.CustomerWebhook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordBroadcast {

  private List<Webhook> baseWebhooks;
  private List<Webhook> staffBaseWebhooks;
  private List<Webhook> liveWebhooks;

  public List<CustomerWebhook> getBaseCustomerWebhooks() {
    return this.getBaseWebhooks().stream()
        .map(
            webhook ->
                CustomerWebhook.builder().url(webhook.getUrl()).type(webhook.getType()).build())
        .collect(Collectors.toList());
  }

  public List<CustomerWebhook> getStaffBaseCustomerWebhooks() {
    return this.getStaffBaseWebhooks().stream()
            .map(
                    webhook ->
                            CustomerWebhook.builder().url(webhook.getUrl()).type(webhook.getType()).build())
            .collect(Collectors.toList());
  }

  public List<CustomerWebhook> getLiveCustomerWebhooks() {
    return this.getLiveWebhooks().stream()
            .map(
                    webhook ->
                            CustomerWebhook.builder().url(webhook.getUrl()).type(webhook.getType()).build())
            .collect(Collectors.toList());
  }
}
