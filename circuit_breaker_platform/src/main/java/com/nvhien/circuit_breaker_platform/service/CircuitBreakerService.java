package com.nvhien.circuit_breaker_platform.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class CircuitBreakerService {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .slowCallRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMinutes(5))
            .slowCallDurationThreshold(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .minimumNumberOfCalls(3)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(3)
            .recordExceptions(IOException.class, TimeoutException.class)
            .build();

    CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("my_cb", circuitBreakerConfig);

    public void handleResultCode(int resultCode) {
        if (resultCode == 0) {
            log.info("CB on success.");
            circuitBreaker.onSuccess(1, TimeUnit.SECONDS);
        } else {
            log.info("CB on error.");
            circuitBreaker.onError(1, TimeUnit.SECONDS, new Throwable());
        }
        circuitBreaker.getEventPublisher().onStateTransition(event -> {
            CircuitBreaker.StateTransition stateTransition = event.getStateTransition();
            switch (stateTransition.getToState()) {
                case OPEN -> {
                    // call maintenance
                    log.error("OPEN CB");
                }
                case HALF_OPEN -> {
                    // call unblock
                    log.error("HALF OPEN CB");
                }
            }
        });
    }
}
