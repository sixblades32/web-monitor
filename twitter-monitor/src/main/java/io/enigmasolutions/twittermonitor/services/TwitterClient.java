package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import io.enigmasolutions.twittermonitor.models.twitter.User;
import io.enigmasolutions.twittermonitor.utils.TwitterClientRestTemplate;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class TwitterClient {

    private final RestTemplate twitterRestTemplate;
    private TwitterConsumer twitterConsumer;

    public TwitterClient(TwitterConsumer twitterConsumer) {
        this.twitterConsumer = twitterConsumer;
        this.twitterRestTemplate = new TwitterClientRestTemplate(twitterConsumer).getRestTemplate();
    }

    public ResponseEntity<User[]> getUser(String path, MultiValueMap<String, String> params){


        String url = "https://api.twitter.com/1.1/"+ path + ".json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);



        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, User[].class);
    }


    public ResponseEntity<String> post(String path, MultiValueMap<String, String> params){

        String url = "https://api.twitter.com/1.1/"+ path + ".json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);



        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);

        return twitterRestTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, String.class);
    }
}
