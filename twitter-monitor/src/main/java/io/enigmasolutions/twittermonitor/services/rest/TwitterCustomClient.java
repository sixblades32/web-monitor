package io.enigmasolutions.twittermonitor.services.rest;

import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterCustomClient {

    // TODO: константы должны быть static
    private final String BASE_PATH = "https://api.twitter.com/1.1/";
    private final String HOME_TIMELINE_PATH = "statuses/home_timeline.json";
    private final String USER_TIMELINE_PATH = "statuses/user_timeline.json";
    private final RestTemplate restTemplate = new RestTemplate();
    private final RestTemplate twitterRestTemplate;
    private final TwitterScraper twitterScraper;

    public TwitterCustomClient(TwitterScraper twitterScraper) {
        this.twitterRestTemplate = new TwitterRestTemplate(twitterScraper.getCredentials()).getRestTemplate();
        this.twitterScraper = twitterScraper;
    }

    public ResponseEntity<TweetResponse[]> getHomeTimelineTweets(MultiValueMap<String, String> params){

        MultiValueMap<String,String> tweetDeckAuth = new LinkedMultiValueMap<>();
        tweetDeckAuth.add("x-csrf-token", twitterScraper.getTweetDeckAuth().getCsrfToken());
        tweetDeckAuth.add("x-act-as-user-id", twitterScraper.getUser().getId());
        tweetDeckAuth.add("Authorization", twitterScraper.getTweetDeckAuth().getBearer());
        tweetDeckAuth.add("Cookie", "auth_token=" + twitterScraper.getTweetDeckAuth().getAuthToken() +
                "; ct0=" + twitterScraper.getTweetDeckAuth().getCsrfToken());

        return getResponseEntity(params, tweetDeckAuth, HOME_TIMELINE_PATH);
    }

    public ResponseEntity<TweetResponse[]> getUserTimelineTweets(MultiValueMap<String, String> params, MultiValueMap<String,String> tweetDeckAuth){

        return getResponseEntity(params, tweetDeckAuth, USER_TIMELINE_PATH);
    }

    // TODO: возможно это стоит сделать и для обычного клиента
    @NotNull
    private ResponseEntity<TweetResponse[]> getResponseEntity(
            MultiValueMap<String, String> params,
            MultiValueMap<String, String> tweetDeckAuth,
            String path
    ) {
        String url = BASE_PATH + path;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(tweetDeckAuth);

        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, TweetResponse[].class);
    }
}
