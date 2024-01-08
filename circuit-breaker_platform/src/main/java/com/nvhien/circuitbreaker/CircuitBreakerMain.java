package com.nvhien.circuitbreaker;

import com.nvhien.circuitbreaker.launcher.CircuitBreakerLauncher;

public class CircuitBreakerMain {
    public static void main(String[] args) {
        CircuitBreakerLauncher circuitBreakerLauncher = new CircuitBreakerLauncher();
        circuitBreakerLauncher.launch();
    }
}
