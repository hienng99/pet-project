package com.nvhien.circuit_breaker_platform.service;

import com.nvhien.circuit_breaker_platform.client.CallMaintainRegisterTask;
import com.nvhien.circuit_breaker_platform.maintenance.MaintainServiceTask;
import com.nvhien.circuit_breaker_platform.utils.CBUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CircuitBreakerService {
    private final MaintainServiceTask maintainServiceTask;

    public CircuitBreakerService(MaintainServiceTask maintainServiceTask) {
        this.maintainServiceTask = maintainServiceTask;
    }

    public void handleResultCode(int resultCode) {
        CircuitBreaker circuitBreaker = CBUtils.getCB();
        if (resultCode == 0) {
            log.info("CB on success.");
            circuitBreaker.onSuccess(1, TimeUnit.SECONDS);
        } else {
            log.info("CB on error.");
            circuitBreaker.onError(100, TimeUnit.SECONDS, new Throwable());
        }

        log.warn("CB status: {}", circuitBreaker.getState().name());
        if (CircuitBreaker.State.OPEN == circuitBreaker.getState()) {
            log.warn("Call maintain.");
            maintainServiceTask.execute();
        }
    }
}
