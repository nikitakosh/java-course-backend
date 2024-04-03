package edu.java.configuration.clients;

import edu.java.utils.LinearRetryBackoffSpec;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public record GitHubClientConfiguration(
        String baseUrl,
        Integer attempts,
        Duration duration,
        String retryPolicy,
        List<Integer> retryCodes
) {
    private static final String EXPONENTIAL_STRING = "exponential";
    private static final String LINEAR_STRING = "linear";

    public Retry getRetry() {
        Retry retry = switch (retryPolicy) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                    attempts,
                    duration
            );
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                    attempts,
                    duration
            );
            default -> Retry.fixedDelay(
                    attempts,
                    duration
            );
        };

        if (retry instanceof RetryBackoffSpec) {
            retry = ((RetryBackoffSpec) retry).filter(customRetryFilter(retryCodes));
        } else {
            retry = ((LinearRetryBackoffSpec) retry).filter(customRetryFilter(retryCodes));
        }
        return retry;
    }

    private Predicate<? super Throwable> customRetryFilter(List<Integer> retryCodes) {
        return e -> e instanceof WebClientResponseException
                && retryCodes.contains(((WebClientResponseException) e).getStatusCode().value());
    }
}
