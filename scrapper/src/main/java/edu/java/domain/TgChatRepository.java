package edu.java.domain;

import edu.java.models.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


public interface TgChatRepository {

    void add(Long tgChatId);
    void remove(Long tgChatId);
    List<Chat> findAll();
    Optional<Chat> findChat(Long tgChatId);
}
