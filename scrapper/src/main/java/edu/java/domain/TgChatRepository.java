package edu.java.domain;

import edu.java.models.Chat;
import java.util.List;
import java.util.Optional;


public interface TgChatRepository {

    Integer add(Long tgChatId);

    Integer remove(Long tgChatId);

    List<Chat> findAll();

    Optional<Chat> find(Long tgChatId);
}
