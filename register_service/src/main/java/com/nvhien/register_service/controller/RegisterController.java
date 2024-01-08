package com.nvhien.register_service.controller;

import com.nvhien.register_service.model.dto.UserRabbitMQMessage;
import com.nvhien.register_service.model.dto.UserRequest;
import com.nvhien.register_service.rabbitmq.RabbitMQProducer;
import com.nvhien.register_service.service.UserService;
import com.nvhien.register_service.util.JsonUtil;
import com.nvhien.register_service.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("api/v1/register")
@Slf4j
public class RegisterController {
    private final UserService userService;
    private final RabbitMQProducer rabbitMQProducer;

    public RegisterController(UserService userService, RabbitMQProducer rabbitMQProducer) {
        this.userService = userService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        Result createResult = userService.create(userRequest);
        if (createResult != Result.SUCCESS) {
            log.error(createResult.getDescription());
            return ResponseEntity.badRequest().body(createResult.getDescription());
        }

        try (ExecutorService sendRabbitExc = Executors.newSingleThreadExecutor()) {
            sendRabbitExc.execute(() -> sendMsgToKafka(userRequest));
        }

        log.info("Save user to DB successfully.");
        return ResponseEntity.ok("User created.");
    }

    public void sendMsgToKafka(UserRequest userRequest) {
        UserRabbitMQMessage userRabbitMQMessage = UserRabbitMQMessage.builder()
                .mailAddress(userRequest.getEmail())
                .fullName(userRequest.getFullName())
                .build();
        Result sendRabbitResult = rabbitMQProducer.sendMessage(JsonUtil.objectToString(userRabbitMQMessage));
        log.info(sendRabbitResult.getDescription());
    }
}
