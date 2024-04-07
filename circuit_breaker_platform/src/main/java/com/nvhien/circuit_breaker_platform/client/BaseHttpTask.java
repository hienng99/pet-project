package com.nvhien.circuit_breaker_platform.client;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseHttpTask {
    HttpClient httpClient = HttpClient.newHttpClient();

    public void execute() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUri()))
                .timeout(Duration.ofSeconds(getTimeout()))
                .headers(getRequestHeader())
                .method(getMethod(), getBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status code: {}", response.statusCode());
            log.info("Body: {}", response.body());
        } catch (Exception exception) {
            log.error("{} execute fail.", this.getClass().getName());
        }
    }

    protected abstract String getUri();

    protected long getTimeout() {
        return 25;
    }

    protected String[] getRequestHeader() {
        List<String> fields = new ArrayList<>();
        fields.add("Content-Type");
        fields.add("application/json");
        return fields.toArray(new String[0]);
    }

    protected abstract String getMethod();

    protected abstract HttpRequest.BodyPublisher getBody();
}
