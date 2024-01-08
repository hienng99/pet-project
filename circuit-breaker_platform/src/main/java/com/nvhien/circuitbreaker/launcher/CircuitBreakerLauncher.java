package com.nvhien.circuitbreaker.launcher;

import com.nvhien.circuitbreaker.task.StartKafkaConsumer;

public class CircuitBreakerLauncher {
    private final StartKafkaConsumer startKafkaConsumer;

    public CircuitBreakerLauncher(StartKafkaConsumer startKafkaConsumer) {
        this.startKafkaConsumer = startKafkaConsumer;
    }

    public void launch() {

    }
}
