package edu.java.scrapper.services.jooq;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.domain.jooq.repositories.JooqLinkRepository;
import edu.java.domain.jooq.tables.pojos.Link;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JooqLinkServiceTest extends IntegrationTest {
    private final JooqTgChatService chatService;
    private final JooqLinkService linkService;
    private final JooqLinkRepository linkRepository;

    @Autowired
    public JooqLinkServiceTest(JooqTgChatService chatService, JooqLinkService linkService, JooqLinkRepository linkRepository) {
        this.chatService = chatService;
        this.linkService = linkService;
        this.linkRepository = linkRepository;
    }


    @Test
    @Transactional
    @Rollback
    public void addTest() {
        chatService.register(1L);
        linkService.add(1L, new AddLinkRequest("https://example.com"));
        Assertions.assertEquals(
                "https://example.com",
                linkService.listAll(1L)
                        .stream()
                        .map(LinkDTO::getUrl)
                        .findFirst()
                        .get()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        chatService.register(1L);
        linkService.add(1L, new AddLinkRequest("https://example.com"));
        linkService.remove(1L, new RemoveLinkRequest("https://example.com"));
        Assertions.assertTrue(
                linkService.listAll(1L).isEmpty()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        Link link = new Link();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.add(link);
        LinkDTO linkDTO = new LinkDTO();
        OffsetDateTime createdAt = OffsetDateTime.now().withNano(0);
        OffsetDateTime updatedAt = OffsetDateTime.now().withNano(0);
        linkDTO.setUrl("https://example.com");
        linkDTO.setCreatedAt(createdAt);
        linkDTO.setUpdatedAt(updatedAt);
        linkDTO.setCommitMessage("commit message");
        linkDTO.setCommitSHA("commit sha");
        linkDTO.setAnswerId(123123L);
        linkDTO.setAnswerOwner("answer owner");
        linkService.update(linkDTO);
        Link linkFromDb = linkRepository.find("https://example.com").get();
        Assertions.assertEquals(createdAt, linkFromDb.getCreatedAt());
        Assertions.assertEquals(updatedAt, linkFromDb.getUpdatedAt());
        Assertions.assertEquals("commit message", linkFromDb.getCommitMessage());
        Assertions.assertEquals("commit sha", linkFromDb.getCommitSha());
        Assertions.assertEquals(123123, linkFromDb.getAnswerId());
        Assertions.assertEquals("answer owner", linkFromDb.getAnswerOwner());
    }

    @Test
    @Transactional
    @Rollback
    public void listAllTest() {
        chatService.register(1L);
        linkService.add(1L, new AddLinkRequest("https://example1.com"));
        linkService.add(1L, new AddLinkRequest("https://example2.com"));
        Assertions.assertEquals(
                List.of("https://example1.com", "https://example2.com"),
                linkService.listAll(1L).stream()
                        .map(LinkDTO::getUrl)
                        .toList()
        );
    }

    @SneakyThrows
    @Test
    @Transactional
    @Rollback
    public void findOldLinksTest() {
        chatService.register(1L);
        linkService.add(1L, new AddLinkRequest("https://example1.com"));
        linkService.add(1L, new AddLinkRequest("https://example2.com"));
        Thread.sleep(3000);
        Assertions.assertEquals(
                List.of("https://example1.com", "https://example2.com"),
                linkService.findOldLinks(3)
                        .stream()
                        .map(LinkDTO::getUrl)
                        .toList()
        );
        Assertions.assertTrue(
                linkService.findOldLinks(10000).isEmpty()
        );
    }
}
