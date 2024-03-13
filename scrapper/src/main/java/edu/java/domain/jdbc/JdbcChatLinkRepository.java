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
                """).params(List.of(tgChatId, linkId))
                .query(ChatLink.class)
                .optional();
    }

    @Override
    public void removeByChat(Long tgChatId) {
        List<Integer> deletedLinkIds = jdbcClient.sql(
                        """
                        DELETE
                        FROM chat_link
                        WHERE chat_link.chat_id = :tgChatId
                        RETURNING link_id
                        """).param("tgChatId", tgChatId)
                .query(Integer.class)
                .list();
        deletedLinkIds.stream().filter(id ->
                jdbcClient.sql(
                                """
                                SElECT link_id FROM chat_link WHERE link_id = :id
                                """)
                        .param("id", id)
                        .query(Integer.class)
                        .optional()
                        .isEmpty()
        ).forEach(id -> jdbcClient.sql("DELETE FROM link WHERE id = :id").param("id", id).update());
    }

    @Override
    public void remove(Long tgChatId, Link link) {
        jdbcClient.sql("DELETE FROM chat_link WHERE chat_link.chat_id = ? AND chat_link.link_id = ?")
                .params(List.of(tgChatId, link.getId()))
                .update();
        if (!linkIsPresent(link)) {
            jdbcClient.sql("DELETE FROM link WHERE id = :id")
                    .param("id", link.getId())
                    .update();
        }
    }

    @Override
    public boolean linkIsPresent(Link link) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE link_id = :id")
                .param("id", link.getId())
                .query(ChatLink.class)
                .optional().isPresent();
    }
}
