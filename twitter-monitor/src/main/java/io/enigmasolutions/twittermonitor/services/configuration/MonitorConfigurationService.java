package io.enigmasolutions.twittermonitor.services.configuration;

import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.db.repositories.TargetRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterConsumerRepository;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterScraperRepository;
import io.enigmasolutions.twittermonitor.exceptions.NoTargetMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.TargetAlreadyAddedException;
import io.enigmasolutions.twittermonitor.models.external.UserStartForm;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.services.monitoring.TwitterHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MonitorConfigurationService {

    private final TwitterHelperService twitterHelperService;
    private final TwitterConsumerRepository twitterConsumerRepository;
    private final TwitterScraperRepository twitterScraperRepository;
    private final TargetRepository targetRepository;

    @Autowired
    public MonitorConfigurationService(TwitterHelperService twitterHelperService,
                                       TwitterConsumerRepository twitterConsumerRepository,
                                       TwitterScraperRepository twitterScraperRepository,
                                       TargetRepository targetRepository) {

        this.twitterHelperService = twitterHelperService;
        this.twitterConsumerRepository = twitterConsumerRepository;
        this.twitterScraperRepository = twitterScraperRepository;
        this.targetRepository = targetRepository;
    }

    public void createConsumer(TwitterConsumer consumer) {

        twitterConsumerRepository.insert(consumer);
    }

    public List<TwitterConsumer> getConsumers() {

        return twitterConsumerRepository.findAll();
    }

    public void deleteConsumer(TwitterConsumer twitterConsumer) {

        String id = twitterConsumer.getId();

        twitterConsumerRepository.deleteById(id);
    }

    public void createScraper(TwitterScraper scraper) {

        twitterScraperRepository.insert(scraper);
    }

    public List<TwitterScraper> getScrapers() {
        return twitterScraperRepository.findAll();
    }

    public void deleteScraper(TwitterScraper twitterScraper) {

        String id = twitterScraper.getId();

        twitterConsumerRepository.deleteById(id);
    }

    public List<Target> getGlobalTargets() {

        return targetRepository.findAll();
    }

    @Transactional
    public void createGlobalTarget(UserStartForm body) {
        User user;

        try {
            user = twitterHelperService.retrieveUser(body.getScreenName());
        } catch (Exception e) {
            throw new NoTwitterUserMatchesException();
        }

        if (twitterHelperService.getBaseTargetsIds().contains(user.getId())) {
            throw new TargetAlreadyAddedException();
        }

        Target target = Target.builder()
                .username(user.getScreenName().toLowerCase())
                .identifier(user.getId())
                .build();

        targetRepository.insert(target);
        twitterHelperService.getBaseTargetsIds().add(user.getId());
    }

    @Transactional
    public void deleteGlobalTarget(UserStartForm body) {
        User user;

        try {
            user = twitterHelperService.retrieveUser(body.getScreenName());
        } catch (Exception e) {
            throw new NoTwitterUserMatchesException();
        }

        if (!twitterHelperService.getBaseTargetsIds().contains(user.getId())) {
            throw new NoTargetMatchesException();
        }

        targetRepository.deleteTargetByIdentifier(user.getId());
        twitterHelperService.getBaseTargetsIds().remove(user.getId());
    }

    public List<String> getTemporaryTargets() {

        return twitterHelperService.getLiveReleaseTargetsScreenNames();
    }

    public void createTemporaryTarget(UserStartForm body) {
        User user;

        try {
            user = twitterHelperService.retrieveUser(body.getScreenName());
        } catch (Exception e) {
            throw new NoTwitterUserMatchesException();
        }

        if (twitterHelperService.getLiveReleaseTargetsIds().contains(user.getId())) {
            throw new TargetAlreadyAddedException();
        }

        twitterHelperService.getLiveReleaseTargetsIds().add(user.getId());
        twitterHelperService.getLiveReleaseTargetsScreenNames().add(user.getScreenName());
    }

    public void deleteTemporaryTarget(UserStartForm body) {
        User user;

        try {
            user = twitterHelperService.retrieveUser(body.getScreenName());
        } catch (Exception e) {
            throw new NoTwitterUserMatchesException();
        }

        if (!twitterHelperService.getLiveReleaseTargetsIds().contains(user.getId())) {
            throw new NoTargetMatchesException();
        }

        twitterHelperService.getLiveReleaseTargetsIds().remove(user.getId());
        twitterHelperService.getLiveReleaseTargetsScreenNames().remove(user.getScreenName());
    }
}
