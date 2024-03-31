package edu.java.bot.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.clients")
public record ClientConfiguration(
        @Bean
        ScrapperClientConfig scrapperClientConfig
) {
    public record ScrapperClientConfig(
            String baseUrl,
            Integer attempts,
            Duration duration,
            String backOffStrategy
    ) {
    }
}
