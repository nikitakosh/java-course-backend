package edu.java.scrapper.domain.jpa;

import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


public class JpaLinkTest extends IntegrationTest {
    private final JpaLinkRepository linkRepository;

    @Autowired
    public JpaLinkTest(JpaLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    public void findByUrlTest() {
        LinkEntity link = new LinkEntity();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.save(link);
        Assertions.assertEquals(
                link,
                linkRepository.findByUrl("https://example.com").get()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void findByIdTest() {
        LinkEntity link = new LinkEntity();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.save(link);
        int linkId = linkRepository.findByUrl("https://example.com").get().getId();
        Assertions.assertEquals(
                link,
                linkRepository.findById(linkId).get()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        LinkEntity link1 = new LinkEntity();
        link1.setUrl("https://example1.com");
        link1.setCreatedAt(OffsetDateTime.now());
        LinkEntity link2 = new LinkEntity();
        link2.setUrl("https://example2.com");
        link2.setCreatedAt(OffsetDateTime.now());
        linkRepository.save(link1);
        linkRepository.save(link2);
        Assertions.assertEquals(
                List.of(link1, link2),
                linkRepository.findAll()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        LinkEntity link = new LinkEntity();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.save(link);
        linkRepository.delete(link);
        Assertions.assertTrue(linkRepository.findByUrl("https://example.com").isEmpty());
    }
}
