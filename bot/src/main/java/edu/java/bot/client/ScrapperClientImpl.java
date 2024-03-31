package edu.java.bot.client;

import edu.java.bot.configuration.ClientConfiguration.ScrapperClientConfig;
import edu.java.bot.controllers.dto.AddLinkRequest;
import edu.java.bot.controllers.dto.ListLinksResponse;
import edu.java.bot.controllers.dto.RemoveLinkRequest;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@RequiredArgsConstructor
public class ScrapperClientImpl implements ScrapperClient {
    public static final String LINKS = "/links";
    public static final String TG_CHAT_ID = "Tg-Chat-Id";
    private final WebClient webClient;
    private final RetryBackoffSpec backoffSpec;

    public ScrapperClientImpl(ScrapperClientConfig clientConfig, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(clientConfig.baseUrl()).build();
        this.backoffSpec = switch (clientConfig.backOffStrategy()) {
            case "constant" -> Retry.fixedDelay(clientConfig.attempts(), clientConfig.duration());
            case "exponential" -> Retry.backoff(clientConfig.attempts(), clientConfig.duration());
            default -> throw new IllegalStateException("Unexpected value: " + clientConfig.backOffStrategy());
        };
    }

    public ScrapperClientImpl(ScrapperClientConfig clientConfig, WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.backoffSpec = switch (clientConfig.backOffStrategy()) {
            case "constant" -> Retry.fixedDelay(clientConfig.attempts(), clientConfig.duration());
            case "exponential" -> Retry.backoff(clientConfig.attempts(), clientConfig.duration());
            default -> throw new IllegalStateException("Unexpected value: " + clientConfig.backOffStrategy());
        };
    }


    @Override
    public void registerChat(Long tgChatId) {
        webClient.post()
                .uri("/tg-chat/{id}", tgChatId)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(backoffSpec.filter(errorFilter()))
                .block();
    }

    private Predicate<? super Throwable> errorFilter() {
        return e -> ((WebClientResponseException) e).getStatusCode().value() == 400;
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
