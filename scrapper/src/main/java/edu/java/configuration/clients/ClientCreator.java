package edu.java.configuration.clients;

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
public class ClientCreator {

    private final WebClient.Builder webClientBuilder;
    private final BotClientConfiguration botClientConfiguration;
    private final GitHubClientConfiguration gitHubClientConfiguration;
    private final StackOverflowClientConfiguration stackOverflowClientConfiguration;

    @Bean
    public BotClient botClient() {
        return new BotClientImpl(botClientConfiguration, webClientBuilder);
    }

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(gitHubClientConfiguration, webClientBuilder);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(stackOverflowClientConfiguration, webClientBuilder);
    }
}
