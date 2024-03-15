package edu.java.domain.jdbc;

import edu.java.domain.LinkDao;
import edu.java.models.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkDao implements LinkDao {
    private final JdbcClient jdbcClient;

    @Override
    public Integer add(Link link) {
        return jdbcClient.sql("INSERT INTO link(url, updated_at, created_at) VALUES (?, ?, ?) RETURNING id")
                .params(link.getUrl(), link.getUpdatedAt(), link.getCreatedAt())
                .query(Integer.class)
                .single();
    }

    @Override
    public Integer remove(String url) {
        return jdbcClient.sql("DELETE FROM link WHERE url = ?")
                .param(url)
                .update();
    }

    @Override
    public void update(Link link) {
        jdbcClient.sql(
                        """
                                UPDATE link
                                SET updated_at = ?,
                                created_at = ?,
                                commit_message = ?,
                                commit_sha = ?,
                                answer_id = ?,
                                answer_owner = ?
                                WHERE url = ?
                                """)
                .params(
                        link.getUpdatedAt(),
                        link.getCreatedAt(),
                        link.getCommitMessage(),
                        link.getCommitSHA(),
                        link.getAnswerId(),
                        link.getAnswerOwner(),
                        link.getUrl()
                )
                .update();
    }


    @Override
    public List<Link> findAll() {
        return jdbcClient.sql("SELECT * FROM link")
                .query(Link.class)
                .list();
    }


    @Override
    public List<Link> findAllByChat(Long tgChatId) {
        return jdbcClient.sql(
                        """
                                SELECT *
                                FROM link
                                JOIN chat_link
                                ON link.id = chat_link.link_id
                                WHERE chat_id = ?
                                """).param(tgChatId)
                .query(Link.class)
                .list();
    }

    @Override
    public Optional<Link> find(String url) {
        return jdbcClient.sql("SELECT * FROM link WHERE link.url = ?")
                .param(url)
                .query(Link.class)
                .optional();
    }
}
