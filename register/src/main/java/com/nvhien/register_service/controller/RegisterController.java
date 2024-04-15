package com.nvhien.register_service.controller;

import com.nvhien.register_service.model.dto.UserRabbitMQMessage;
import com.nvhien.register_service.model.dto.UserRequest;
import com.nvhien.register_service.rabbitmq.producer.RabbitMQCBProducer;
import com.nvhien.register_service.rabbitmq.producer.RabbitMQRegisterProducer;
import com.nvhien.register_service.service.UserService;
import com.nvhien.register_service.util.JsonUtil;
import com.nvhien.register_service.util.RegisterConst;
import com.nvhien.register_service.util.Result;
import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("api/v1/register")
@Slf4j
public class RegisterController {
    private final UserService userService;
    private final RabbitMQRegisterProducer rabbitMQRegisterProducer;
    private final RabbitMQCBProducer rabbitMQCBProducer;

    public RegisterController(UserService userService, RabbitMQRegisterProducer rabbitMQRegisterProducer, RabbitMQCBProducer rabbitMQCBProducer) {
        this.userService = userService;
        this.rabbitMQRegisterProducer = rabbitMQRegisterProducer;
        this.rabbitMQCBProducer = rabbitMQCBProducer;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        UnleashConfig unleashConfig = UnleashConfig.builder()
                .appName("default")
                .instanceId("localhost")
                .unleashAPI("http://localhost:4242/api/")
                .apiKey("default:development.unleash-insecure-api-token")
                .synchronousFetchOnInitialisation(true)
                .build();

        Unleash unleash = new DefaultUnleash(unleashConfig);

        if (!unleash.isEnabled("register_service_toogle")) {
            log.warn("Unleash off.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found.");
        }

        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                String serviceStatus = jedis.get(RegisterConst.SERVICE_NAME);
                if (!"1".equals(serviceStatus)) {
                    return ResponseEntity.ok("Service is under maintenance.");
                }
            }
        }
        Result createResult = userService.create(userRequest);

        try (ExecutorService sendRabbitExc = Executors.newSingleThreadExecutor()) {
            sendRabbitExc.execute(() -> sendResultCodeToKafka(createResult.getCode()));
        }

        if (createResult != Result.SUCCESS) {
            log.warn(createResult.getDescription());
            return ResponseEntity.badRequest().body(createResult.getDescription());
        }

        try (ExecutorService sendRabbitExc = Executors.newSingleThreadExecutor()) {
            sendRabbitExc.execute(() -> sendRegisterMsgToKafka(userRequest));
        }

        log.info("Save user to DB successfully.");
        return ResponseEntity.ok("User created.");
    }

    public void sendRegisterMsgToKafka(UserRequest userRequest) {
        UserRabbitMQMessage userRabbitMQMessage = UserRabbitMQMessage.builder()
                .mailAddress(userRequest.getEmail())
                .fullName(userRequest.getFullName())
                .build();
        Result sendRabbitResult = rabbitMQRegisterProducer.sendMessage(JsonUtil.objectToString(userRabbitMQMessage));
        log.info(sendRabbitResult.getDescription());
    }

    public void sendResultCodeToKafka(int resultCode) {
        Result sendRabbitResult = rabbitMQCBProducer.sendMessage(Integer.toString(resultCode));
        log.info(sendRabbitResult.getDescription());
    }
}
