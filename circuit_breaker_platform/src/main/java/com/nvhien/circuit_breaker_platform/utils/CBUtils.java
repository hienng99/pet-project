package com.nvhien.circuit_breaker_platform.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class CBUtils {
    private CircuitBreaker circuitBreakerInstant;

    public synchronized CircuitBreaker getCB() {
        if (circuitBreakerInstant == null) {
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofSeconds(1))
                    .permittedNumberOfCallsInHalfOpenState(3)
                    .minimumNumberOfCalls(1)
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .slidingWindowSize(3)
                    .build();

            circuitBreakerInstant = CircuitBreaker.of("my_circuit_breaker", circuitBreakerConfig);
        }
        return circuitBreakerInstant;
    }
}
