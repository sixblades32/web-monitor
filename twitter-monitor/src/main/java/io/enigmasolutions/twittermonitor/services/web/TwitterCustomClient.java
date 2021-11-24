package io.enigmasolutions.twittermonitor.services.web;

import io.enigmasolutions.twittermonitor.db.models.documents.RestTemplateProxy;
import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import io.enigmasolutions.twittermonitor.models.twitter.base.TweetResponse;
import io.enigmasolutions.twittermonitor.models.twitter.graphql.GraphQLResponse;
import io.enigmasolutions.twittermonitor.models.twitter.v2.V2Response;
import java.net.URI;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterCustomClient {

  private static final String BASE_API_PATH = "https://api.twitter.com/1.1/";
  private static final String GRAPHQL_API_PATH = "https://twitter.com/i/api/graphql/wmAsSZ-tfs22k90iIA7X1w/UserTweetsAndReplies";
  private static final String V2_BASE_API_PATH = "https://twitter.com/i/api/2/";
  private final RestTemplate restTemplate = new RestTemplate();
  private final RestTemplate proxiedRestTemplate = new RestTemplate();
  private final TwitterScraper twitterScraper;

  public TwitterCustomClient(TwitterScraper twitterScraper) {
    this.twitterScraper = twitterScraper;
  }

  public TwitterCustomClient(TwitterScraper twitterScraper, RestTemplateProxy proxy) {
    this.twitterScraper = twitterScraper;

    String host = proxy.getHost();
    int port = Integer.parseInt(proxy.getPort());
    String username = proxy.getLogin();
    String password = proxy.getPassword();

    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
        new AuthScope(host, port),
        new UsernamePasswordCredentials(username, password)
    );

    HttpHost myProxy = new HttpHost(host, port);
    HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider)
        .disableCookieManagement();

    HttpClient httpClient = clientBuilder.build();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setHttpClient(httpClient);

    proxiedRestTemplate.setRequestFactory(factory);
  }


  public ResponseEntity<TweetResponse[]> getBaseApiTimelineTweets(
      MultiValueMap<String, String> params, String timelinePath) {
    MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

    return getResponseEntity(params, tweetDeckAuth, timelinePath);
  }

  public ResponseEntity<TweetResponse[]> getProxiedBaseApiTimelineTweets(
      MultiValueMap<String, String> params, String timelinePath) {
    MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

    return getProxiedResponseEntity(params, tweetDeckAuth, timelinePath);
  }

  public ResponseEntity<GraphQLResponse> getGraphQLApiTimelineTweets(
      MultiValueMap<String, String> params) {
    MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

    return getGraphQLResponseEntity(params, tweetDeckAuth);
  }

  public ResponseEntity<V2Response> getV2BaseTimelineTweets(MultiValueMap<String, String> params,
      String timelinePath) {
    MultiValueMap<String, String> tweetDeckAuth = generateAuthData();

    return getV2ResponseEntity(params, tweetDeckAuth, timelinePath);
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

  private ResponseEntity<TweetResponse[]> getResponseEntity(
      MultiValueMap<String, String> params,
      MultiValueMap<String, String> tweetDeckAuth,
      String path
  ) {
    return restTemplate.exchange(
        generateRequestEntity(params, tweetDeckAuth, path, BASE_API_PATH),
        TweetResponse[].class
    );
  }

  private ResponseEntity<TweetResponse[]> getProxiedResponseEntity(
      MultiValueMap<String, String> params,
      MultiValueMap<String, String> tweetDeckAuth,
      String path) {
    return proxiedRestTemplate.exchange(
        generateRequestEntity(params, tweetDeckAuth, path, BASE_API_PATH),
        TweetResponse[].class
    );
  }

  private ResponseEntity<V2Response> getV2ResponseEntity(
      MultiValueMap<String, String> params,
      MultiValueMap<String, String> tweetDeckAuth,
      String path
  ) {
    return proxiedRestTemplate.exchange(
        generateRequestEntity(params, tweetDeckAuth, path, V2_BASE_API_PATH),
        V2Response.class
    );
  }

  private RequestEntity<Void> generateRequestEntity(
      MultiValueMap<String, String> params,
      MultiValueMap<String, String> tweetDeckAuth,
      String path, String baseApiPath) {

    String url = baseApiPath + path;

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParams(params);

    HttpHeaders headers = new HttpHeaders();
    headers.addAll(tweetDeckAuth);

    return RequestEntity
        .get(builder.toUriString())
        .headers(headers)
        .build();
  }

  private ResponseEntity<GraphQLResponse> getGraphQLResponseEntity(
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
