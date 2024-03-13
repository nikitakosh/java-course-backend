package edu.java.domain.jdbc;

import edu.java.domain.TgChatRepository;
import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.exceptions.NotExistentChatException;
import edu.java.models.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository implements TgChatRepository {
    private final JdbcClient jdbcClient;
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcChatLinkRepository linkRepository;
    @Override
    public void add(Long tgChatId) {
        if (findChat(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException("chat is already exist");
        }
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)").param("id", tgChatId).update();
    }

    @Override
    public void remove(Long tgChatId) {
        if (findChat(tgChatId).isEmpty()) {
            throw new NotExistentChatException("chat is not exist");
        }
        jdbcClient.sql(
                """
                DELETE
                FROM chat
                WHERE chat.id = :id
                """
                ).param("id", tgChatId)
                .update();
        chatLinkRepository.removeByChat(tgChatId);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT id FROM chat").query(Chat.class).list();
    }

    @Override
    public Optional<Chat> findChat(Long tgChatId) {
        return jdbcClient.sql("SELECT id FROM chat WHERE id = :id").param("id", tgChatId)
                .query(Chat.class)
                .optional();
    }
}
