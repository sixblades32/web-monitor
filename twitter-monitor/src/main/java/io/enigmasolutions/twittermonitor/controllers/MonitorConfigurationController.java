package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.monitoring.TwitterHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
public class MonitorConfigurationController {

    private final TwitterHelperService twitterHelperService;
    private final TwitterConsumerRepository twitterConsumerRepository;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TargetRepository targetRepository;

    @Autowired
    public MonitorConfigurationController(TwitterHelperService twitterHelperService,
                                          TwitterConsumerRepository twitterConsumerRepository,
                                          TwitterScraperRepository twitterScraperRepository,
                                          TargetRepository targetRepository) {
        this.twitterHelperService = twitterHelperService;
        this.twitterConsumerRepository = twitterConsumerRepository;
        this.twitterScraperRepository = twitterScraperRepository;
        this.targetRepository = targetRepository;
    }

    @PostMapping("/consumers")
    public void createConsumer(@RequestBody TwitterConsumer consumer) {

        twitterConsumerRepository.insert(consumer);
    }

    @GetMapping("/consumers")
    public List<TwitterConsumer> getConsumers(){
        return twitterConsumerRepository.findAll();
    }

    @DeleteMapping("/consumers")
    public void deleteConsumer(@RequestBody TwitterConsumer twitterConsumer){

        String id = twitterConsumer.getId();

        twitterConsumerRepository.deleteById(id);
    }

    @PostMapping("/scrapers")
    public void createScraper(@RequestBody TwitterScraper scraper) {

        twitterScraperRepository.insert(scraper);
    }

    @GetMapping("/scrapers")
    public List<TwitterScraper> getScrapers(){
        return twitterScraperRepository.findAll();
    }

    @DeleteMapping("/scrapers")
    public void deleteScraper(@RequestBody TwitterScraper twitterScraper){

        String id = twitterScraper.getId();

        twitterConsumerRepository.deleteById(id);
    }

    @GetMapping("/targets/global")
    public List<Target> getGlobalTargets(){
        return targetRepository.findAll();
    }

    @PostMapping("/targets/global")
    public void createGlobalTarget(@RequestBody UserStartForm body){
        User user = twitterHelperService.retrieveUser(body.getScreenName());

        Target target = Target.builder()
                .username(user.getScreenName().toLowerCase())
                .identifier(user.getId())
                .build();

        targetRepository.insert(target);
        twitterHelperService.getCommonTargetIds().add(user.getId());
    }

    @DeleteMapping("/targets/global")
    public void deleteGlobalTarget(@RequestBody UserStartForm body){
        User user = twitterHelperService.retrieveUser(body.getScreenName());

        targetRepository.deleteTargetByIdentifier(user.getId());
        twitterHelperService.getCommonTargetIds().remove(user.getId());
    }

    @GetMapping("/targets/temporary")
    public List<String> getTemporaryTargets(){
        return twitterHelperService.getAdvancedTargetIds();
    }

    @PostMapping("/targets/temporary")
    public void createTemporaryTarget(@RequestBody UserStartForm body){
        User user = twitterHelperService.retrieveUser(body.getScreenName());

        twitterHelperService.getAdvancedTargetIds().add(user.getId());
    }

    @DeleteMapping("/targets/temporary")
    public void deleteTemporaryTarget(@RequestBody UserStartForm body){
        User user = twitterHelperService.retrieveUser(body.getScreenName());

        twitterHelperService.getAdvancedTargetIds().remove(user.getId());
    }


}
