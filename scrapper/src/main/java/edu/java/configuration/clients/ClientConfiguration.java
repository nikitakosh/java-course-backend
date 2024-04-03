package edu.java.configuration.clients;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "clients")
public record ClientConfiguration(
        @Bean
        GitHubClientConfiguration gitHubClientConfig,
        @Bean
        StackOverflowClientConfiguration stackOverflowClientConfig,
        @Bean
        BotClientConfiguration botClientConfig
) {
}
