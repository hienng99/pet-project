package com.nvhien.circuit_breaker_platform.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
public abstract class BaseHttpClient {
    HttpClient httpClient = HttpClient.newHttpClient();

    public void execute() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUri()))
                .timeout(Duration.ofSeconds(getTimeout()))
                .headers(getRequestHeader())
                .method(getMethod(), getBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Status code: {}", response.statusCode());
        log.info("Body: {}", response.body());
    }

    protected abstract String getUri();

    protected long getTimeout() {
        return 25;
    }

    protected abstract String getRequestHeader();

    protected abstract String getMethod();

    protected abstract HttpRequest.BodyPublisher getBody();
}
