package edu.java.scrapper.domain.jdbc;


import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.models.Link;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcLinkTest extends IntegrationTest {
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcLinkTest(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link link = new Link();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.add(link);
        Optional<Link> linkFromDB = linkRepository.find(link.getUrl());
        Assertions.assertTrue(linkFromDB.isPresent());
        Assertions.assertEquals("https://example.com", linkFromDB.get().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        Link link = new Link();
        link.setUrl("https://example.com");
        link.setCreatedAt(OffsetDateTime.now());
        linkRepository.add(link);
        linkRepository.remove(link.getUrl());
        Optional<Link> linkFromDB = linkRepository.find(link.getUrl());
        Assertions.assertTrue(linkFromDB.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Link link1 = new Link();
        Link link2 = new Link();
        link1.setUrl("https://example1.com");
        link1.setCreatedAt(OffsetDateTime.now());
        link2.setUrl("https://example2.com");
        link2.setCreatedAt(OffsetDateTime.now());
        linkRepository.add(link1);
        linkRepository.add(link2);
        List<String> linksFromDB = linkRepository.findAll().stream().map(Link::getUrl).toList();
        Assertions.assertEquals(
                List.of("https://example1.com", "https://example2.com"),
                linksFromDB
        );
    }
}
