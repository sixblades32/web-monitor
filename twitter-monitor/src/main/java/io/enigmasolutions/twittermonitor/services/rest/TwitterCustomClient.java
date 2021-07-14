package io.enigmasolutions.twittermonitor.services.rest;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class TwitterCustomClient {

    private static final String BASE_API_PATH = "https://api.twitter.com/1.1/";
    private static final String GRAPHQL_API_PATH = "https://twitter.com/i/api/graphql/omtTuEwTr6DFLIREto-MMg/UserTweets";
    private final RestTemplate restTemplate = new RestTemplate();
    private final TwitterScraper twitterScraper;

    public TwitterCustomClient(TwitterScraper twitterScraper) {
        this.twitterScraper = twitterScraper;
    }

    public ResponseEntity<TweetResponse[]> getBaseApiTimelineTweets(MultiValueMap<String, String> params, String timelinePath) {
        MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

        return getResponseEntity(params, tweetDeckAuth, timelinePath);
    }

    public ResponseEntity<GraphQLResponse> getGraphQLApiTimelineTweets(MultiValueMap<String, String> params) {
        MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

        return getResponseEntity(params, tweetDeckAuth);
    }

    private MultiValueMap<String, String> generateAuthData() {
        MultiValueMap<String, String> authData = new LinkedMultiValueMap<>();
        authData.add("x-csrf-token", twitterScraper.getTweetDeckAuth().getCsrfToken());
        authData.add("x-act-as-user-id", twitterScraper.getTwitterUser().getTwitterId());
        authData.add("Authorization", twitterScraper.getTweetDeckAuth().getBearer());
        authData.add("Cookie", "auth_token=" + twitterScraper.getTweetDeckAuth().getAuthToken() +
                "; ct0=" + twitterScraper.getTweetDeckAuth().getCsrfToken());

        return authData;
    }

    @NotNull
    private ResponseEntity<TweetResponse[]> getResponseEntity(
            MultiValueMap<String, String> params,
            MultiValueMap<String, String> tweetDeckAuth,
            String path
    ) {
        String url = BASE_API_PATH + path;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(tweetDeckAuth);

        RequestEntity<Void> requestEntity = RequestEntity
                .get(builder.toUriString())
                .headers(headers)
                .build();

        return restTemplate.exchange(requestEntity, TweetResponse[].class);
    }

    @NotNull
    private ResponseEntity<GraphQLResponse> getResponseEntity(
            MultiValueMap<String, String> params,
            MultiValueMap<String, String> tweetDeckAuth
    ) {
        URI uri = UriComponentsBuilder.fromHttpUrl(GRAPHQL_API_PATH)
                .queryParams(params)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(tweetDeckAuth);

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .headers(headers)
                .build();

        return restTemplate.exchange(requestEntity, GraphQLResponse.class);
    }

    public TwitterScraper getTwitterScraper() {
        return twitterScraper;
    }
}
