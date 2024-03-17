package edu.java.services;

import edu.java.services.dto.LinkDTO;
import java.util.List;

public interface TgChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);

    List<Long> findChatsByLink(LinkDTO link);
}
