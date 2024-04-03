package edu.java.domain.jooq.repositories;

import edu.java.domain.jooq.tables.pojos.ChatLink;
import edu.java.domain.jooq.tables.pojos.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import static edu.java.domain.jooq.Tables.CHAT_LINK;

@Service
@RequiredArgsConstructor
public class JooqChatLinkRepository {
    private final DSLContext context;

    public Optional<ChatLink> find(Long tgChatId, Integer linkId) {
        return context.selectFrom(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .and(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .fetchOptionalInto(ChatLink.class);
    }

    public void removeByChat(Long tgChatId) {
        context.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .execute();
    }

    public Integer remove(Long tgChatId, Link link) {
        return context.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(link.getId()))
                .and(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .execute();
    }

    public Integer add(Integer linkId, Long tgChatId) {
        return context.insertInto(CHAT_LINK)
                .set(CHAT_LINK.LINK_ID, linkId)
                .set(CHAT_LINK.CHAT_ID, tgChatId)
                .execute();
    }

    public List<ChatLink> findAll() {
        return context.selectFrom(CHAT_LINK)
                .fetchInto(ChatLink.class);
    }

    public List<Long> findChatsByLink(Integer linkId) {
        return context.select(CHAT_LINK.CHAT_ID)
                .from(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .fetchInto(Long.class);
    }

    public boolean isLinkPresent(Integer linkId) {
        return context.selectFrom(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .fetchOptionalInto(ChatLink.class)
                .isPresent();
    }
}
