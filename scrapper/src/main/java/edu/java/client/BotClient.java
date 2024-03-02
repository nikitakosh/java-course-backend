package edu.java.client;


import edu.java.controllers.dto.LinkUpdate;

public interface BotClient {
    void sendMessage(LinkUpdate linkUpdate);
}
