package edu.java.domain.jdbc;

import edu.java.domain.jdbc.models.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository {
    private final JdbcClient jdbcClient;

    public Integer add(Long tgChatId) {
        return jdbcClient.sql("INSERT INTO chat(id) VALUES (?)")
                .param(tgChatId)
                .update();
    }

    public Integer remove(Long tgChatId) {
        return jdbcClient.sql("DELETE FROM chat WHERE id = ?")
                .param(tgChatId)
                .update();
    }

    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT id FROM chat").query(Chat.class).list();
    }


    public Optional<Chat> find(Long tgChatId) {
        return jdbcClient.sql("SELECT id FROM chat WHERE id = :id").param("id", tgChatId)
                .query(Chat.class)
                .optional();
    }
}
