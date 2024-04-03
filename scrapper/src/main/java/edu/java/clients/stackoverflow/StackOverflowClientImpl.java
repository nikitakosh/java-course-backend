package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.AnswerItemResponse;
import edu.java.clients.stackoverflow.dto.AnswerResponse;
import edu.java.clients.stackoverflow.dto.QuestionItemResponse;
import edu.java.clients.stackoverflow.dto.QuestionResponse;
import edu.java.configuration.clients.StackOverflowClientConfiguration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient webClient;
    private final StackOverflowClientConfiguration clientConfiguration;

    public StackOverflowClientImpl(
            StackOverflowClientConfiguration clientConfiguration,
            WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder
                .baseUrl(clientConfiguration.baseUrl())
                .build();
        this.clientConfiguration = clientConfiguration;
    }

    public StackOverflowClientImpl(
            StackOverflowClientConfiguration clientConfiguration,
            WebClient.Builder webClientBuilder,
            String baseUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    public QuestionItemResponse fetchQuestion(String id) {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/questions/{id}?site=stackoverflow", id)
                        .retrieve()
                        .bodyToMono(QuestionResponse.class)
                        .retryWhen(clientConfiguration.getRetry())
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
                .retryWhen(clientConfiguration.getRetry())
                .block()
                .items()
                .getFirst();
    }

}
