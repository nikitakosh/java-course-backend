package edu.java.domain.jdbc;

import edu.java.domain.jdbc.models.ChatLink;
import edu.java.domain.jdbc.models.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository {
    private final JdbcClient jdbcClient;


    public Optional<ChatLink> find(Long tgChatId, Integer linkId) {
        return jdbcClient.sql(
                        """
                                SELECT chat_id, link_id
                                FROM chat_link
                                WHERE chat_id = ? AND link_id = ?
                                """).params(tgChatId, linkId)
                .query(ChatLink.class)
                .optional();
    }

    public Integer remove(Long tgChatId, Link link) {
        return jdbcClient.sql("DELETE FROM chat_link WHERE chat_link.chat_id = ? AND chat_link.link_id = ?")
                .params(List.of(tgChatId, link.getId()))
                .update();
    }

    public Integer add(Integer linkId, Long tgChatId) {
        return jdbcClient.sql("INSERT INTO chat_link VALUES (?, ?)")
                .params(linkId, tgChatId)
                .update();
    }

    public List<ChatLink> findAll() {
        return jdbcClient.sql("SELECT * FROM chat_link").query(ChatLink.class).list();
    }

    public List<Long> findChatsByLink(Integer linkId) {
        return jdbcClient.sql("SELECT chat_id FROM chat_link WHERE link_id = :id")
                .param("id", linkId)
                .query(Long.class)
                .list();
    }

    public boolean isLinkPresent(Integer linkId) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE link_id = :id")
                .param("id", linkId)
                .query(ChatLink.class)
                .optional()
                .isPresent();
    }


}
