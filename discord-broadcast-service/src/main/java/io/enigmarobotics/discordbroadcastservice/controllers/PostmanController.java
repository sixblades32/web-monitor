package io.enigmarobotics.discordbroadcastservice.controllers;

import io.enigmarobotics.discordbroadcastservice.services.PostmanService;
import io.enigmasolutions.broadcastmodels.FollowRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postman")
public class PostmanController {

  private final PostmanService postmanService;

  public PostmanController(PostmanService postmanService) {
    this.postmanService = postmanService;
  }

  @PostMapping("/request")
  public void sendFollowRequestEmbed(@RequestBody FollowRequest followRequest) {
    postmanService.sendFollowRequestEmbed(followRequest);
  }
}
