package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.AnswerItemResponse;
import edu.java.clients.stackoverflow.dto.AnswerResponse;
import edu.java.clients.stackoverflow.dto.QuestionItemResponse;
import edu.java.clients.stackoverflow.dto.QuestionResponse;
import java.util.Objects;
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
    public QuestionItemResponse fetchQuestion(String id) {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/questions/{id}?site=stackoverflow", id)
                        .retrieve()
                        .bodyToMono(QuestionResponse.class)
                        .block())
                .items()
                .getFirst();
    }

    @Override
    public AnswerItemResponse fetchAnswer(String id) {
        return webClient.get()
                .uri("/questions/{id}/answers?order=desc&sort=creation&site=stackoverflow", id)
                .retrieve()
                .bodyToMono(AnswerResponse.class)
                .block()
                .items()
                .getFirst();
    }

}
