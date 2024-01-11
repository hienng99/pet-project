package com.nvhien.circuit_breaker_platform.controller;

import com.nvhien.circuit_breaker_platform.service.CircuitBreakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class CircuitBreakerController {
    private final CircuitBreakerService circuitBreakerService;

    public CircuitBreakerController(CircuitBreakerService circuitBreakerService) {
        this.circuitBreakerService = circuitBreakerService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        log.info("Receive message: {}", message);
        try {
            int resultCode = Integer.parseInt(message);
            circuitBreakerService.handleResultCode(resultCode);
        } catch (ClassCastException classCastException) {

        }
    }
}
