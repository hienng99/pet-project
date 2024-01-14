package com.nvhien.circuit_breaker_platform.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CircuitBreakerService {
    private static CircuitBreaker circuitBreakerInstant;

    public void handleResultCode(int resultCode) {
        CircuitBreaker circuitBreaker = getCB();
        if (resultCode == 0) {
            log.info("CB on success.");
            circuitBreaker.onSuccess(1, TimeUnit.SECONDS);
        } else {
            log.info("CB on error.");
            circuitBreaker.onError(100, TimeUnit.SECONDS, new Throwable());
        }
        CircuitBreaker.EventPublisher eventPublisher = circuitBreaker.getEventPublisher();
        eventPublisher.onStateTransition(event -> {
            CircuitBreaker.StateTransition stateTransition = event.getStateTransition();
            if (stateTransition.getToState() == CircuitBreaker.State.OPEN) {
                log.error("OPEN CB");
            } else if (stateTransition.getToState() == CircuitBreaker.State.HALF_OPEN) {
                log.error("HALF OPEN CB");
            }
        });
    }

    private synchronized CircuitBreaker getCB() {
        if (circuitBreakerInstant == null) {
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofSeconds(1))
                    .permittedNumberOfCallsInHalfOpenState(3)
                    .minimumNumberOfCalls(3)
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .slidingWindowSize(3)
                    .build();

            circuitBreakerInstant = CircuitBreaker.of("my_circuit_breaker", circuitBreakerConfig);
        }
        return circuitBreakerInstant;
    }
}
