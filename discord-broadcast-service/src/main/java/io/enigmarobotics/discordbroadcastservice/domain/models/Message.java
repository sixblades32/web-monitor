package io.enigmarobotics.discordbroadcastservice.domain.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private String content;
  private List<Embed> embeds;


}
