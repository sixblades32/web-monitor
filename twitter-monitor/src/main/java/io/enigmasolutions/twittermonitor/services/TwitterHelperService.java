package io.enigmasolutions.twittermonitor.services;

import io.enigmasolutions.twittermonitor.db.models.TwitterConsumer;
import io.enigmasolutions.twittermonitor.db.repositories.TwitterClientRepository;
import io.enigmasolutions.twittermonitor.models.twitter.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterHelperService {
    private final TwitterClientRepository twitterClientRepository;

    private List<TwitterClient> twitterClients;

    @Autowired
    public TwitterHelperService(TwitterClientRepository twitterClientRepository) {
        this.twitterClientRepository = twitterClientRepository;
    }

    @PostConstruct
    public void initTwitterClients(){
        List<TwitterConsumer> clients = twitterClientRepository.findAll();

        this.twitterClients = clients.stream().map(TwitterClient::new).collect(Collectors.toList());
    }

    public void retrieveUser(String screenName){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("screen_name", screenName);
        TwitterClient currentClient = twitterClients.get(0);
        twitterClients.remove(0);
        twitterClients.add(currentClient);
        System.out.println(Arrays.asList(currentClient.getUser("users/lookup", params).getBody()).get(0));
    }
}
