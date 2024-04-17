package com.nvhien.register_service.unleash;


import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UnleashUtils {
    private Unleash unleash;

    public synchronized Unleash getUnleash() {
        if (unleash == null) {
            UnleashConfig unleashConfig = UnleashConfig.builder()
                    .appName("default")
                    .instanceId("172.23.224.165")
                    .unleashAPI("http://172.23.224.165:4242/api/")
                    .apiKey("default:development.unleash-insecure-api-token")
                    .synchronousFetchOnInitialisation(true)
                    .build();

            unleash = new DefaultUnleash(unleashConfig);
        }
        return unleash;
    }
}
