package edu.java.configuration;

import edu.java.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.domain.jooq.repositories.JooqLinkRepository;
import edu.java.domain.jooq.repositories.JooqTgChatRepository;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public LinkService linkService(
            JooqLinkRepository linkRepository,
            JooqTgChatRepository chatRepository,
            JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqLinkService(linkRepository, chatRepository, chatLinkRepository);
    }

    @Bean
    public TgChatService tgChatService(
            JooqTgChatRepository chatRepository,
            JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqTgChatService(chatRepository, chatLinkRepository);
    }
}
