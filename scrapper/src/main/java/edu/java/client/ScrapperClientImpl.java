package edu.java.client;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientImpl implements ScrapperClient {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    public static final String SCRAPPER_API_V_1_0_LINKS = "scrapper-api/v1.0/links";
    public static final String SCRAPPER_API_V_1_0_TG_CHAT_ID = "scrapper-api/v1.0/tg-chat/{id}";
    private final WebClient webClient;

    public ScrapperClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public ScrapperClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }


    @Override
    public void registerChat(Integer tgChatId) {
        webClient.post()
                .uri(SCRAPPER_API_V_1_0_TG_CHAT_ID, tgChatId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void deleteChat(Integer tgChatId) {
        webClient.delete()
                .uri(SCRAPPER_API_V_1_0_TG_CHAT_ID, tgChatId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public ListLinksResponse getLinks(Integer tgChatId) {
        return webClient.get()
                .uri(SCRAPPER_API_V_1_0_LINKS)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
    }

    @Override
    public void addLink(Integer tgChatId, AddLinkRequest addLinkRequest) {
        webClient.post()
                .uri(SCRAPPER_API_V_1_0_LINKS)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(addLinkRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest) {
        webClient.method(HttpMethod.DELETE)
                .uri(SCRAPPER_API_V_1_0_LINKS)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(removeLinkRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
