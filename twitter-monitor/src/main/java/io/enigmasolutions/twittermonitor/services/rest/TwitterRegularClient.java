package io.enigmasolutions.twittermonitor.services.rest;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.common.FollowsList;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterRegularClient {

    private final static String BASE_PATH = "https://api.twitter.com/1.1";
    private final static String USERS_PATH = "/users/lookup.json";
    private final static String FOLLOWS_PATH = "/friends/ids.json";

    private final RestTemplate twitterRestTemplate;

    public TwitterRegularClient(TwitterConsumer twitterConsumer) {
        this.twitterRestTemplate = new TwitterRestTemplate(twitterConsumer.getCredentials()).getRestTemplate();
    }

    public ResponseEntity<User[]> getUser(MultiValueMap<String, String> params) {

        RequestEntity<Void> requestEntity = buildRequestEntity(USERS_PATH, params);

        return twitterRestTemplate.exchange(requestEntity, User[].class);
    }

    public ResponseEntity<FollowsList> getFollows(MultiValueMap<String, String> params){

        RequestEntity<Void> requestEntity = buildRequestEntity(FOLLOWS_PATH, params);

        return twitterRestTemplate.exchange(requestEntity, FollowsList.class);
    }

    public RequestEntity<Void> buildRequestEntity(String path, MultiValueMap<String, String> params){
        String url = BASE_PATH + path;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);

       return RequestEntity
                .get(builder.toUriString())
                .build();
    }
}
