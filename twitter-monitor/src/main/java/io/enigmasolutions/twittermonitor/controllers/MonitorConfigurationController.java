package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.services.configuration.MonitorConfigurationService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class MonitorConfigurationController {

  private final MonitorConfigurationService monitorConfigurationService;

  @Autowired
  public MonitorConfigurationController(MonitorConfigurationService monitorConfigurationService) {
    this.monitorConfigurationService = monitorConfigurationService;
  }

  @PostMapping("/consumers")
  public void createConsumer(@RequestBody TwitterConsumer consumer) {

    monitorConfigurationService.createConsumer(consumer);
  }

  @GetMapping("/consumers")
  public List<TwitterConsumer> getConsumers() {

    return monitorConfigurationService.getConsumers();
  }

  @DeleteMapping("/consumers")
  public void deleteConsumer(@RequestBody TwitterConsumer twitterConsumer) {

    monitorConfigurationService.deleteConsumer(twitterConsumer);
  }

  @PostMapping("/scrapers")
  public void createScraper(@RequestBody TwitterScraper scraper) {

    monitorConfigurationService.createScraper(scraper);
  }

  @GetMapping("/scrapers")
  public List<TwitterScraper> getScrapers() {
    return monitorConfigurationService.getScrapers();
  }

  @DeleteMapping("/scrapers")
  public void deleteScraper(@RequestBody TwitterScraper twitterScraper) {

    monitorConfigurationService.deleteScraper(twitterScraper);
  }

  @GetMapping("/targets/global")
  public List<Target> getGlobalTargets() {

    return monitorConfigurationService.getGlobalTargets();
  }

  @PostMapping("/targets/global")
  public void createGlobalTarget(@RequestBody UserStartForm body) {

    monitorConfigurationService.createGlobalTarget(body);
  }

  @DeleteMapping("/targets/global")
  public void deleteGlobalTarget(@RequestBody UserStartForm body) {

    monitorConfigurationService.deleteGlobalTarget(body);
  }

  @GetMapping("/targets/temporary")
  public List<String> getTemporaryTargets() {

    return monitorConfigurationService.getTemporaryTargets();
  }

  @PostMapping("/targets/temporary")
  public void createTemporaryTarget(@RequestBody UserStartForm body) {

    monitorConfigurationService.createTemporaryTarget(body);
  }

  @DeleteMapping("/targets/temporary")
  public void deleteTemporaryTarget(@RequestBody UserStartForm body) {

    monitorConfigurationService.deleteTemporaryTarget(body);
  }

  @PostMapping("/request")
  public void createRequest(@RequestBody FollowRequest followRequest) {
    monitorConfigurationService.createFollowRequest(followRequest);
  }
}
