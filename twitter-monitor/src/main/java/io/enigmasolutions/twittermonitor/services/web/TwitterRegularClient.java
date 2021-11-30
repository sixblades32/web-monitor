package io.enigmasolutions.twittermonitor.services.web;

import io.enigmasolutions.twittermonitor.db.models.references.Credentials;
import io.enigmasolutions.twittermonitor.db.models.references.Proxy;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import io.enigmasolutions.twittermonitor.models.twitter.common.FollowingData;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwitterRegularClient {

  private final static String BASE_PATH = "https://api.twitter.com/1.1";
  private final static String USERS_PATH = "/users/lookup.json";
  private final static String FOLLOWINGS_PATH = "/friends/ids.json";
  private final static String FOLLOW_PATH = "/friendships/create.json";

  private final RestTemplate twitterRestTemplate;
  private RestTemplate proxiedRestTemplate;
  private String clientId;

  public TwitterRegularClient(Credentials credentials) {
    this.twitterRestTemplate = new TwitterRestTemplate(
        credentials).getRestTemplate();
  }

  public TwitterRegularClient(Credentials credentials, String clientId, Proxy proxy){
    this(credentials);
    this.clientId = clientId;

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

    this.proxiedRestTemplate = new TwitterRestTemplate(
            credentials).getRestTemplate();
    proxiedRestTemplate.setRequestFactory(factory);
  }

  public ResponseEntity<User[]> getUser(MultiValueMap<String, String> params) {

    RequestEntity<Void> requestEntity = buildGetRequestEntity(USERS_PATH, params);

    return twitterRestTemplate.exchange(requestEntity, User[].class);
  }

  public ResponseEntity<FollowingData> getFollows(MultiValueMap<String, String> params) {

    RequestEntity<Void> requestEntity = buildGetRequestEntity(FOLLOWINGS_PATH, params);

    return twitterRestTemplate.exchange(requestEntity, FollowingData.class);
  }

  public RequestEntity<Void> buildGetRequestEntity(String path, MultiValueMap<String, String> params) {

    return RequestEntity
        .get(buildUri(path, params).toUriString())
        .build();
  }

  public RequestEntity<Void> buildPostRequestEntity(String path, MultiValueMap<String, String> params) {

    return RequestEntity
            .post(buildUri(path, params).toUriString())
            .build();
  }

  public UriComponentsBuilder buildUri(String path, MultiValueMap<String, String> params){
    String url = BASE_PATH + path;

    return UriComponentsBuilder.fromHttpUrl(url)
            .queryParams(params);
  }

  public String getClientId() {
    return clientId;
  }

  public ResponseEntity<Void> follow(MultiValueMap<String, String> params) {
    RequestEntity<Void> requestEntity = buildPostRequestEntity(FOLLOW_PATH, params);

    return proxiedRestTemplate.exchange(requestEntity, Void.class);
  }
}
