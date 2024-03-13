package edu.java.domain;

import edu.java.models.Chat;
import edu.java.models.ChatLink;
import edu.java.models.Link;
import java.util.Optional;

public interface ChatLinkRepository {
    Optional<ChatLink> find(Integer linkId, Long tgChatId);
    void removeByChat(Long tgChatId);
    void remove(Long tgChatId, Link link);
    boolean linkIsPresent(Link link);

}
