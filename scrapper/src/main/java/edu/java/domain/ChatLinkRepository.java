package edu.java.domain;

import edu.java.models.ChatLink;
import edu.java.models.Link;
import java.util.List;
import java.util.Optional;

public interface ChatLinkRepository {
    Optional<ChatLink> find(Integer linkId, Long tgChatId);

    Integer remove(Long tgChatId, Link link);

    Integer add(Integer linkId, Long tgChatId);

    List<ChatLink> findAll();

    List<Long> findChatsByLink(Link link);

    boolean isLinkPresent(Link link);
}
