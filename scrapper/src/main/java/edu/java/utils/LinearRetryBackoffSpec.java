package edu.java.utils;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.springframework.retry.ExhaustedRetryException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
import reactor.util.context.ContextView;
import reactor.util.retry.Retry;

public final class LinearRetryBackoffSpec extends Retry {
    public final static Duration MAX_BACKOFF = Duration.ofMillis(Long.MAX_VALUE);
    public final Duration minBackoff;
    public final Supplier<Scheduler> backoffSchedulerSupplier;

    public final long maxAttempts;

    public final Predicate<Throwable> errorFilter;

    LinearRetryBackoffSpec(
            ContextView retryContext,
            long max,
            Predicate<? super Throwable> aThrowablePredicate,
            Duration minBackoff,
            Supplier<Scheduler> backoffSchedulerSupplier) {
        super(retryContext);
        this.maxAttempts = max;
        this.errorFilter = aThrowablePredicate::test;
        this.minBackoff = minBackoff;
        this.backoffSchedulerSupplier = backoffSchedulerSupplier;
    }

    public static LinearRetryBackoffSpec linearBackoff(int maxAttempts, Duration minDelay) {
        return new LinearRetryBackoffSpec(
                Context.empty(),
                maxAttempts,
                e -> true,
                minDelay,
                Schedulers::parallel
        );
    }

    public LinearRetryBackoffSpec filter(Predicate<? super Throwable> errorFilter) {
        return new LinearRetryBackoffSpec(
                this.retryContext,
                this.maxAttempts,
                Objects.requireNonNull(errorFilter, "errorFilter"),
                this.minBackoff,
                this.backoffSchedulerSupplier);
    }

    @Override
    public Flux<Long> generateCompanion(Flux<RetrySignal> retrySignals) {
        return Flux.deferContextual(cv ->
                retrySignals.contextWrite(cv)
                        .concatMap(retryWhenState -> {
                            RetrySignal copy = retryWhenState.copy();
                            Throwable currentFailure = copy.failure();
                            long iteration = copy.totalRetries();

                            if (currentFailure == null) {
                                return Mono.error(new IllegalStateException(
                                        "Retry.RetrySignal#failure() not expected to be null"));
                            }

                            if (!errorFilter.test(currentFailure)) {
                                return Mono.error(currentFailure);
                            }

                            if (iteration >= maxAttempts) {
                                return Mono.error(new ExhaustedRetryException("Retry exhausted: " + this));
                            }

                            Duration nextBackoff;
                            try {
                                nextBackoff = minBackoff.multipliedBy(iteration + 1);
                                if (nextBackoff.compareTo(MAX_BACKOFF) > 0) {
                                    nextBackoff = MAX_BACKOFF;
                                }
                            } catch (ArithmeticException overflow) {
                                nextBackoff = MAX_BACKOFF;
                            }

                            return Mono.delay(nextBackoff, backoffSchedulerSupplier.get()).contextWrite(cv);
                        })
                        .onErrorStop()
        );
    }
}
