package edu.java.scrapper.services.jpa;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.domain.jpa.entity.ChatEntity;
import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JpaTgChatServiceTest extends IntegrationTest {
    private final JpaTgChatRepository chatRepository;
    private final JpaTgChatService chatService;
    private final JpaLinkService linkService;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public JpaTgChatServiceTest(JpaTgChatRepository chatRepository, JpaTgChatService chatService, JpaLinkService linkService, JpaLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.chatService = chatService;
        this.linkService = linkService;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    public void registerTest() {
        chatService.register(1L);
        ChatEntity chat = new ChatEntity();
        chat.setId(1L);
        Assertions.assertEquals(
                chat,
                chatRepository.findById(1L).get()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterTest() {
        chatService.register(1L);
        chatService.unregister(1L);
        Assertions.assertTrue(chatRepository.findById(1L).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByLinkTest() {
        chatService.register(1L);
        chatService.register(2L);
        linkService.add(1L, new AddLinkRequest("https://example.com"));
        linkService.add(2L, new AddLinkRequest("https://example.com"));
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(linkRepository.findByUrl("https://example.com").get().getId());
        Assertions.assertEquals(
                List.of(1L, 2L),
                chatService.findChatsByLink(linkDTO)
        );
    }
}
