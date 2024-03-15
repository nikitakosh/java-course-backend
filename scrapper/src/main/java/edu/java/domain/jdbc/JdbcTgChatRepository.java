package edu.java.domain.jdbc;

import edu.java.domain.TgChatRepository;
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

    @Override
    public Integer add(Long tgChatId) {
        return jdbcClient.sql("INSERT INTO chat(id) VALUES (?)")
                .param(tgChatId)
                .update();
    }

    @Override
    public Integer remove(Long tgChatId) {
        return jdbcClient.sql("DELETE FROM chat WHERE id = ?")
                .param(tgChatId)
                .update();
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT id FROM chat").query(Chat.class).list();
    }

    @Override
    public Optional<Chat> find(Long tgChatId) {
        return jdbcClient.sql("SELECT id FROM chat WHERE id = :id").param("id", tgChatId)
                .query(Chat.class)
                .optional();
    }
}
