package edu.java.services;

import edu.java.models.Link;
import java.util.List;

public interface TgChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);

    List<Long> findChatsByLink(Link link);
}
