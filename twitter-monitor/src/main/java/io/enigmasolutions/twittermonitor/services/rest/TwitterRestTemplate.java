package io.enigmasolutions.twittermonitor.services.rest;

import io.enigmasolutions.twittermonitor.db.models.Credentials;
import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class TwitterRestTemplate extends AbstractOAuth1ApiBinding {

    public TwitterRestTemplate(Credentials credentials) {
        super(credentials.getConsumerKey(),
                credentials.getConsumerSecret(),
                credentials.getToken(),
                credentials.getTokenSecret());
    }

    @Override
    public RestTemplate getRestTemplate() {
        return super.getRestTemplate();
    }
}
