package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.repositories.JooqTgChatRepository;
import edu.java.domain.jooq.tables.pojos.Chat;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqTgChatTest extends IntegrationTest {
    private final JooqTgChatRepository chatRepository;

    @Autowired
    public JooqTgChatTest(JooqTgChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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
