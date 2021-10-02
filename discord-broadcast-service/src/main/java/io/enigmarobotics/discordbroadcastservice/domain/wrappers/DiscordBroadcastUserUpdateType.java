package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Embed;
import io.enigmarobotics.discordbroadcastservice.utils.DiscordUtils;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import java.util.List;

public enum DiscordBroadcastUserUpdateType {
  NAME {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getName(),
          updatedUserInfo.getName(),
          discordEmbedColorConfig.getUserUpdates());
    }
  },
  LOCATION {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getLocation(),
          updatedUserInfo.getLocation(),
          discordEmbedColorConfig.getUserUpdates());
    }
  },
  SCREEN_NAME {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getLogin(),
          updatedUserInfo.getLogin(),
          discordEmbedColorConfig.getUserUpdates());
    }

    @Override
    public String toString() {
      return "SCREEN NAME";
    }
  },
  DESCRIPTION {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getDescription(),
          updatedUserInfo.getDescription(),
          discordEmbedColorConfig.getUserUpdates());
    }
  },
  URL {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getStatusUrl(),
          updatedUserInfo.getStatusUrl(),
          discordEmbedColorConfig.getUserUpdates());
    }
  },
  IMAGE {
    @Override
    public Embed generateUserUpdateEmbed(
        List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig) {

      TwitterUser oldUserInfo = userUpdates.get(0);
      TwitterUser updatedUserInfo = userUpdates.get(1);

      return DiscordUtils.generateUserUpdatesEmbed(
          this.toString(),
          updatedUserInfo,
          oldUserInfo.getIcon().replace("normal", "400x400"),
          updatedUserInfo.getIcon().replace("normal", "400x400"),
          discordEmbedColorConfig.getUserUpdates());
    }
  };

  public abstract Embed generateUserUpdateEmbed(
      List<TwitterUser> userUpdates, DiscordEmbedColorConfig discordEmbedColorConfig);
}
