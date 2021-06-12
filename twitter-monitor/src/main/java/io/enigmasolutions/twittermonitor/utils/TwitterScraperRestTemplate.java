package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.web.client.RestTemplate;

public class TwitterScraperRestTemplate extends AbstractOAuth1ApiBinding {

    public TwitterScraperRestTemplate(TwitterScraper twitterScraper){
        super(twitterScraper.getCredentials().getConsumerKey(),
                twitterScraper.getCredentials().getConsumerSecret(),
                twitterScraper.getCredentials().getToken(),
                twitterScraper.getCredentials().getTokenSecret());
    }

    @Override
    public RestTemplate getRestTemplate() {
        return super.getRestTemplate();
    }
}
