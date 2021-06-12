package io.enigmasolutions.twittermonitor.utils;

import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class TwitterClientRestTemplate extends AbstractOAuth1ApiBinding {

    public TwitterClientRestTemplate(TwitterConsumer twitterConsumer) {
        super(twitterConsumer.getCredentials().getConsumerKey(),
                twitterConsumer.getCredentials().getConsumerSecret(),
                twitterConsumer.getCredentials().getToken(),
                twitterConsumer.getCredentials().getTokenSecret());
    }

    @Override
    public RestTemplate getRestTemplate() {
        return super.getRestTemplate();
    }
}
