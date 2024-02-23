package edu.java.configuration;

import edu.java.client.ScrapperClient;
import edu.java.client.ScrapperClientImpl;
import edu.java.github.GitHubClient;
import edu.java.github.GitHubClientImpl;
import edu.java.stackoverflow.StackOverflowClient;
import edu.java.stackoverflow.StackOverflowClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final WebClient.Builder webClientBuilder;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClientImpl(webClientBuilder);
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
