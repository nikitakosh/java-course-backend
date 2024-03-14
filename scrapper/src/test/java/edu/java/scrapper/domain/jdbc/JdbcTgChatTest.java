package edu.java.scrapper.domain.jdbc;

import edu.java.domain.jdbc.JdbcTgChatRepository;
import edu.java.models.Chat;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcTgChatTest extends IntegrationTest {
    private final JdbcTgChatRepository chatRepository;
    private final JdbcClient jdbcClient;

    @Autowired
    public JdbcTgChatTest(JdbcTgChatRepository chatRepository, JdbcClient jdbcClient) {
        this.chatRepository = chatRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        chatRepository.add(1L);
        Optional<Chat> chat = chatRepository.find(1L);
        Assertions.assertTrue(chat.isPresent());
        Assertions.assertEquals(1L, chat.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        chatRepository.add(1L);
        chatRepository.remove(1L);
        Optional<Chat> chat = chatRepository.find(1L);
        Assertions.assertTrue(chat.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        List<Long> chats = chatRepository.findAll().stream().map(Chat::getId).toList();
        Assertions.assertEquals(List.of(1L, 2L), chats);
    }
}
