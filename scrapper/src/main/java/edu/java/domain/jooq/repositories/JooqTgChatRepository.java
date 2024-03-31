package edu.java.domain.jooq.repositories;

import static edu.java.domain.jooq.Tables.CHAT;
import edu.java.domain.jooq.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JooqTgChatRepository {

    private final DSLContext context;

    public Integer add(Long tgChatId) {

        return context.insertInto(CHAT)
                .set(CHAT.ID, tgChatId)
                .execute();
    }

    public Integer remove(Long tgChatId) {
        return context.delete(CHAT)
                .where(CHAT.ID.eq(tgChatId))
                .execute();
    }

    public List<Chat> findAll() {
        return context.selectFrom(CHAT)
                .fetchInto(Chat.class);
    }

    public Optional<Chat> find(Long tgChatId) {
        return context.selectFrom(CHAT)
                .where(CHAT.ID.eq(tgChatId))
                .fetchOptionalInto(Chat.class);
    }
}
