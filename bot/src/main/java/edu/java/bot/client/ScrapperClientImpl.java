package edu.java.bot.client;

import edu.java.bot.controllers.dto.AddLinkRequest;
import edu.java.bot.controllers.dto.ListLinksResponse;
import edu.java.bot.controllers.dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientImpl implements ScrapperClient {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String LINKS = "/links";
    public static final String TG_CHAT_ID = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public ScrapperClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }


    @Override
    public void registerChat(Long tgChatId) {
        webClient.post()
                .uri("/tg-chat/{id}", tgChatId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void deleteChat(Long tgChatId) {
        webClient.delete()
                .uri("tg-chat/{id}", tgChatId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public ListLinksResponse getLinks(Long tgChatId) {
        return webClient.get()
                .uri(LINKS)
                .header(TG_CHAT_ID, String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
    }

    @Override
    public void addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        webClient.post()
                .uri(LINKS)
                .header(TG_CHAT_ID, String.valueOf(tgChatId))
                .bodyValue(addLinkRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        webClient.method(HttpMethod.DELETE)
                .uri(LINKS)
                .header(TG_CHAT_ID, String.valueOf(tgChatId))
                .bodyValue(removeLinkRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
