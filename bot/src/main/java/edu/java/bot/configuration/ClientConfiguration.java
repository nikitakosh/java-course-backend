package edu.java.bot.configuration;

import edu.java.bot.client.BotClient;
import edu.java.bot.client.BotClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final WebClient.Builder webClientBuilder;

    @Bean
    public BotClient botClient() {
        return new BotClientImpl(webClientBuilder);
    }
}
