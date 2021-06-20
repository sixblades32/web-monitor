package io.enigmasolutions.twittermonitor.services.rest;

import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import io.enigmasolutions.twittermonitor.models.twitter.FollowsList;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterClient {

    // TODO: константы должны быть static
    private final String BASE_PATH = "https://api.twitter.com/1.1/";
    private final String USERS_PATH = "users/lookup.json";
    private final String FOLLOWS_PATH = "friends/ids.json";

    private final RestTemplate twitterRestTemplate;
    // TODO: final
    private TwitterConsumer twitterConsumer;

    public TwitterClient(TwitterConsumer twitterConsumer) {
        this.twitterConsumer = twitterConsumer;
        this.twitterRestTemplate = new TwitterRestTemplate(twitterConsumer.getCredentials()).getRestTemplate();
    }

    public ResponseEntity<User[]> getUser(MultiValueMap<String, String> params){

        String url = BASE_PATH + USERS_PATH;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);

        // TODO: заменить на RequestEntity auth-service/src/main/java/io/enigmasolutions/webmonitor/authservice/services/DiscordValidationService.java
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, User[].class);
    }

    public ResponseEntity<FollowsList> getFollows(MultiValueMap<String, String> params){

        String url = BASE_PATH + FOLLOWS_PATH;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);

        // TODO: заменить на RequestEntity auth-service/src/main/java/io/enigmasolutions/webmonitor/authservice/services/DiscordValidationService.java
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, FollowsList.class);
    }
}
