package com.nvhien.circuit_breaker_platform.service;

import com.nvhien.circuit_breaker_platform.utils.CBUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CircuitBreakerService {


    public void handleResultCode(int resultCode) {
        CircuitBreaker circuitBreaker = CBUtils.getCB();
        log.warn("CB status: {}", circuitBreaker.getState().name());
        if (resultCode == 0) {
            log.info("CB on success.");
            circuitBreaker.onSuccess(1, TimeUnit.SECONDS);
        } else {
            log.info("CB on error.");
            circuitBreaker.onError(100, TimeUnit.SECONDS, new Throwable());
        }

        switch (circuitBreaker.getState()) {
            case CircuitBreaker.State.OPEN -> {
            }

        }
    }


}
