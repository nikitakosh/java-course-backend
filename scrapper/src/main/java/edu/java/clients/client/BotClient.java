package edu.java.clients.client;


import edu.java.controllers.dto.LinkUpdate;

public interface BotClient {
    LinkUpdate sendMessage(LinkUpdate linkUpdate);
}
