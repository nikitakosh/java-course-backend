package edu.java.scrapper.services.jdbc;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JdbcTgChatServiceTest extends IntegrationTest {
    private final JdbcTgChatService chatService;
    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkService linkService;
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcTgChatServiceTest(JdbcTgChatService chatService, JdbcTgChatRepository chatRepository, JdbcLinkService linkService, JdbcLinkRepository linkRepository) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
        this.linkService = linkService;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    public void registerTest() {
        chatService.register(1L);
        Assertions.assertEquals(
                1L,
                chatRepository.find(1L).get().getId()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterTest() {
        chatService.register(1L);
        chatService.unregister(1L);
        Assertions.assertTrue(chatRepository.find(1L).isEmpty());
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
        linkDTO.setId(linkRepository.find("https://example.com").get().getId());
        Assertions.assertEquals(
                List.of(1L, 2L),
                chatService.findChatsByLink(linkDTO)
        );
    }
}
