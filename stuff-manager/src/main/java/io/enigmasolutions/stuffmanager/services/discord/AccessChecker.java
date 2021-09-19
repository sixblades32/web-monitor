package io.enigmasolutions.stuffmanager.services.discord;

import discord4j.discordjson.possible.Possible;
import org.springframework.stereotype.Service;

@Service
public class AccessChecker {

    public Boolean isBot(Possible<Boolean> possibleBot){

        return !possibleBot.isAbsent() && possibleBot.get();
    }
}
