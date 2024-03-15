package edu.java.clients.client;


import edu.java.controllers.dto.LinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;


public class BotClientImpl implements BotClient {
    public static final String BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    public BotClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public BotClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public LinkUpdate sendMessage(LinkUpdate linkUpdate) {
        return webClient.post()
                .uri("/updates")
                .bodyValue(linkUpdate)
                .retrieve()
                .bodyToMono(LinkUpdate.class)
                .block();
    }
}
