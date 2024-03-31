package edu.java.domain.jooq.repositories;

import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;
import edu.java.domain.jooq.tables.pojos.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqLinkRepository {

    private final DSLContext context;

    public Integer add(Link link) {
        return context.insertInto(LINK)
                .set(LINK.URL, link.getUrl())
                .set(LINK.CREATED_AT, link.getCreatedAt())
                .execute();
    }

    public Integer remove(String url) {
        return context.delete(LINK)
                .where(LINK.URL.eq(url))
                .execute();
    }

    public void update(Link link) {
        context.update(LINK)
                .set(LINK.UPDATED_AT, link.getUpdatedAt())
                .set(LINK.CREATED_AT, link.getCreatedAt())
                .set(LINK.COMMIT_MESSAGE, link.getCommitMessage())
                .set(LINK.COMMIT_SHA, link.getCommitSha())
                .set(LINK.ANSWER_ID, link.getAnswerId())
                .set(LINK.ANSWER_OWNER, link.getAnswerOwner())
                .where(LINK.URL.eq(link.getUrl()))
                .execute();
    }

    public List<Link> findAll() {
        return context.selectFrom(LINK)
                .fetchInto(Link.class);
    }


    public List<Link> findAllByChat(Long tgChatId) {
        return context.selectFrom(LINK.join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID)))
                .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .fetchInto(Link.class);
    }

    public Optional<Link> find(String url) {
        return context.selectFrom(LINK)
                .where(LINK.URL.eq(url))
                .fetchOptionalInto(Link.class);
    }
}
