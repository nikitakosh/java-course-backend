package edu.java.client;


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
    public void sendMessage(LinkUpdate linkUpdate) {
        webClient.post()
                .uri("/bot-api/v1.0/updates")
                .bodyValue(linkUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
