package com.nvhien.register_service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Result {
    SUCCESS(0, "Success"),
    SEND_MSG_TO_RABBIT_FAILED(1, "Send message to RabbitMQ failed"),
    USERNAME_ALREADY_EXIST(2, "The username already exist."),
    EMAIL_ALREADY_EXIST(3, "The email already exist.");

    private final int code;
    private final String description;
}
