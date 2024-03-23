package edu.java.bot.sender;

import java.util.List;

public interface MessageSender {
    void send(List<Long> tgChatIds, String message);
}
