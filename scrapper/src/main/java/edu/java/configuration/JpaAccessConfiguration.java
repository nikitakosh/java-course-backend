package edu.java.configuration;

import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
            JpaLinkRepository linkRepository,
            JpaTgChatRepository chatRepository
    ) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    public TgChatService tgChatService(
            JpaLinkRepository linkRepository,
            JpaTgChatRepository chatRepository
    ) {
        return new JpaTgChatService(linkRepository, chatRepository);
    }
}
