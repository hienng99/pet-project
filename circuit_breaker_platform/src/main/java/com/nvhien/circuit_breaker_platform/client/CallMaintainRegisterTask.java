package com.nvhien.circuit_breaker_platform.client;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;

@Component
public class CallMaintainRegisterTask extends BaseHttpTask {
    @Override
    protected String getUri() {
        return "path";
    }

    @Override
    protected String getMethod() {
        return HttpMethod.GET.name();
    }

    @Override
    protected HttpRequest.BodyPublisher getBody() {
        JSONObject jsonObject = new JSONObject();
        return HttpRequest.BodyPublishers.ofString(jsonObject.toString());
    }
}
