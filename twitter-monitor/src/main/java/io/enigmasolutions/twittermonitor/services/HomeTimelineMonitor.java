package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.repositories.TwitterClientRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomeTimelineMonitor {

    private TwitterScraperRepository twitterScraperRepository;

    @Autowired
    public HomeTimelineMonitor(TwitterScraperRepository twitterScraperRepository) {
        this.twitterScraperRepository = twitterScraperRepository;
    }

    public void initMonitors(){
        System.out.println(twitterScraperRepository.findAll());
    }
}
