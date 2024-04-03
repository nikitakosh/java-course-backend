package edu.java.configuration;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcTgChatRepository;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
            JdbcLinkRepository linkRepository,
            JdbcTgChatRepository chatRepository,
            JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcLinkService(linkRepository, chatRepository, chatLinkRepository);
    }

    @Bean
    public TgChatService tgChatService(
            JdbcTgChatRepository chatRepository,
            JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcTgChatService(chatRepository, chatLinkRepository);
    }
}
