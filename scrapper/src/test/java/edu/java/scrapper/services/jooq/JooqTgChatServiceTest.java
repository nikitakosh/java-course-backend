package edu.java.scrapper.services.jooq;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.domain.jooq.repositories.JooqLinkRepository;
import edu.java.domain.jooq.repositories.JooqTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JooqTgChatServiceTest extends IntegrationTest {
    private final JooqTgChatService chatService;
    private final JooqTgChatRepository chatRepository;
    private final JooqLinkRepository linkRepository;
    private final JooqLinkService linkService;


    @Autowired
    public JooqTgChatServiceTest(JooqTgChatRepository chatRepository, JooqLinkRepository linkRepository, JooqChatLinkRepository chatLinkRepository) {
        this.chatService = new JooqTgChatService(chatRepository, chatLinkRepository);
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.linkService = new JooqLinkService(linkRepository, chatRepository, chatLinkRepository);
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
