package edu.java.domain.jdbc;

import edu.java.domain.LinkDao;
import edu.java.exceptions.NotExistentChatException;
import edu.java.exceptions.NotExistentLinkException;
import edu.java.models.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkDao implements LinkDao {
    private final JdbcClient jdbcClient;
    private final JdbcTgChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;
    @Override
    public void add(Long tgChatId, Link link) {
        if (chatRepository.findChat(tgChatId).isEmpty()) {
            throw new NotExistentLinkException("chat is not exist");
        }
        int linkId;
        if (findLink(link.getUrl()).isPresent()) {
            linkId = jdbcClient.sql("SELECT id FROM link WHERE url = :url")
                    .param("url", link.getUrl())
                    .query(Integer.class)
                    .single();
        } else {
            linkId = jdbcClient.sql("INSERT INTO link(url, updated_at) VALUES (?, ?) RETURNING id")
                    .params(List.of(link.getUrl(), link.getUpdatedAt()))
                    .query(Integer.class)
                    .single();
        }
        if (chatLinkRepository.find(linkId, tgChatId).isEmpty()) {
            jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES(?, ?)")
                    .params(List.of(linkId, tgChatId))
                    .update();
        }
    }

    @Override
    public void remove(Long tgChatId, String url) {
        if (chatRepository.findChat(tgChatId).isEmpty()) {
            throw new NotExistentChatException("chat not exist");
        }
        Optional<Link> link = findLink(url);
        if (link.isEmpty()) {
            throw new NotExistentLinkException("link not exist");
        }
        chatLinkRepository.remove(tgChatId, link.get());
    }


    @Override
    public List<Link> findAll(Long tgChatId) {
        return jdbcClient.sql(
                """
                SELECT link.id, link.url, link.updated_at
                FROM link
                JOIN chat_link
                ON link.id = chat_link.link_id
                WHERE chat_link.chat_id = :tgChatId
                """)
                .param("tgChatId", tgChatId)
                .query(Link.class)
                .list();
    }

    @Override
    public Optional<Link> findLink(String url) {
        return jdbcClient.sql("SELECT * FROM link WHERE link.url = :url")
                .param("url", url)
                .query(Link.class)
                .optional();
    }
}
