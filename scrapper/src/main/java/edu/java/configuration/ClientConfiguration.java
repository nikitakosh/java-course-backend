package edu.java.configuration;

import edu.java.clients.client.BotClient;
import edu.java.clients.client.BotClientImpl;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.StackOverflowClientImpl;
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

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(webClientBuilder);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(webClientBuilder);
    }
}
