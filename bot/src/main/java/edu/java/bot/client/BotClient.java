package edu.java.bot.client;

import edu.java.bot.controllers.dto.LinkUpdate;

public interface BotClient {
    LinkUpdate sendMessage(LinkUpdate linkUpdate);
}
