package edu.java.scrapper.domain.jpa;

import edu.java.domain.jpa.entity.ChatEntity;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class JpaChatTest extends IntegrationTest {
    private final JpaTgChatRepository chatRepository;

    @Autowired
    public JpaChatTest(JpaTgChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Test
    @Transactional
    @Rollback
    public void findByIdTest() {
        ChatEntity chat = new ChatEntity();
        chat.setId(1L);
        chatRepository.save(chat);
        Assertions.assertEquals(
                chat,
                chatRepository.findById(1L).get()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByIdTest() {
        ChatEntity chat = new ChatEntity();
        chat.setId(1L);
        chatRepository.save(chat);
        chatRepository.deleteById(1L);
        Assertions.assertTrue(chatRepository.findById(1L).isEmpty());
    }
}
