package edu.java.clients.stackoverflow;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class StackOverflowClientImpl implements StackOverflowClient {

    public static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL)
                .build();
    }

    public StackOverflowClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public ItemResponse fetchQuestion(String id) {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/questions/{id}?site=stackoverflow", id)
                        .retrieve()
                        .bodyToMono(QuestionResponse.class)
                        .block())
                .items()
                .getFirst();
    }

    @Override
    public boolean isSupport(String url) {
        Pattern pattern = Pattern.compile("^https://api\\.stackexchange\\.com/question/[^/]+?site=stackoverflow");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
