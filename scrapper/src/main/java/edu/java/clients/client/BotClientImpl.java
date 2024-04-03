package edu.java.clients.client;


import edu.java.configuration.clients.BotClientConfiguration;
import edu.java.controllers.dto.LinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;


public class BotClientImpl implements BotClient {
    private final WebClient webClient;
    private final BotClientConfiguration clientConfiguration;

    public BotClientImpl(BotClientConfiguration clientConfiguration, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(clientConfiguration.baseUrl()).build();
        this.clientConfiguration = clientConfiguration;
    }

    public BotClientImpl(BotClientConfiguration clientConfiguration,
                         WebClient.Builder webClientBuilder,
                         String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    public LinkUpdate sendMessage(LinkUpdate linkUpdate) {
        return webClient.post()
                .uri("/updates")
                .bodyValue(linkUpdate)
                .retrieve()
                .bodyToMono(LinkUpdate.class)
                .retryWhen(clientConfiguration.getRetry())
                .block();
    }
}
