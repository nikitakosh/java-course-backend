package edu.java.scrapper.services.jpa;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JpaLinkServiceTest extends IntegrationTest {
    private final JpaLinkService linkService;
    private final JpaTgChatService chatService;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public JpaLinkServiceTest(JpaLinkRepository linkRepository, JpaTgChatRepository chatRepository) {
        this.linkService = new JpaLinkService(linkRepository, chatRepository);
        this.chatService = new JpaTgChatService(linkRepository, chatRepository);
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
        LinkEntity link = new LinkEntity();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.save(link);
        LinkDTO linkDTO = new LinkDTO();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();
        linkDTO.setUrl("https://example.com");
        linkDTO.setCreatedAt(createdAt);
        linkDTO.setUpdatedAt(updatedAt);
        linkDTO.setCommitMessage("commit message");
        linkDTO.setCommitSHA("commit sha");
        linkDTO.setAnswerId(123123L);
        linkDTO.setAnswerOwner("answer owner");
        linkService.update(linkDTO);
        LinkEntity linkFromDb = linkRepository.findByUrl("https://example.com").get();
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
