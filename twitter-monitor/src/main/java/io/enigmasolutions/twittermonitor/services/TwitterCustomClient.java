package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterScraper;
import io.enigmasolutions.twittermonitor.utils.TwitterScraperRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterCustomClient {
    private final RestTemplate twitterScraperRestTemplate;

    public TwitterCustomClient(TwitterScraper twitterScraper) {
        this.twitterScraperRestTemplate = new TwitterScraperRestTemplate(twitterScraper).getRestTemplate();
    }

    public ResponseEntity<String> get(String path, MultiValueMap<String, String> params, MultiValueMap<String,String> tweetDeckAuth){

        String url = "https://api.twitter.com/1.1/"+ path + ".json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);



        HttpHeaders headers = new HttpHeaders();
        headers.addAll(tweetDeckAuth);

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterScraperRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> post(String path, MultiValueMap<String, String> params, MultiValueMap<String,String> tweetDeckAuth){

        String url = "https://api.twitter.com/1.1/"+ path + ".json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);



        HttpHeaders headers = new HttpHeaders();
        headers.addAll(tweetDeckAuth);

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterScraperRestTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, String.class);
    }
}
