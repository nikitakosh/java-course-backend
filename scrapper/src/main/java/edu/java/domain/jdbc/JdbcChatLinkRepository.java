package edu.java.domain.jdbc;

import edu.java.domain.ChatLinkRepository;
import edu.java.models.ChatLink;
import edu.java.models.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcClient jdbcClient;


    @Override
    public Optional<ChatLink> find(Integer linkId, Long tgChatId) {
        return jdbcClient.sql(
                        """
                                SELECT chat_id, link_id
                                FROM chat_link
                                WHERE chat_id = ? AND link_id = ?
                                """).params(tgChatId, linkId)
                .query(ChatLink.class)
                .optional();
    }

    @Override
    public Integer remove(Long tgChatId, Link link) {
        return jdbcClient.sql("DELETE FROM chat_link WHERE chat_link.chat_id = ? AND chat_link.link_id = ?")
                .params(List.of(tgChatId, link.getId()))
                .update();
    }

    @Override
    public Integer add(Integer linkId, Long tgChatId) {
        return jdbcClient.sql("INSERT INTO chat_link VALUES (?, ?)")
                .params(linkId, tgChatId)
                .update();
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcClient.sql("SELECT * FROM chat_link").query(ChatLink.class).list();
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        return jdbcClient.sql("SELECT chat_id FROM chat_link WHERE link_id = :id")
                .param("id", link.getId())
                .query(Long.class)
                .list();
    }

    @Override
    public boolean isLinkPresent(Link link) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE link_id = :id")
                .param("id", link.getId())
                .query(ChatLink.class)
                .optional()
                .isPresent();
    }


}
