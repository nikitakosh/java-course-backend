package edu.java.client;


import edu.java.controllers.dto.LinkUpdate;

public interface BotClient {
    LinkUpdate sendMessage(LinkUpdate linkUpdate);
}
