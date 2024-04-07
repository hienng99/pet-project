package com.nvhien.register_service.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRabbitMQMessage {
    private String mailAddress;
    private String fullName;
}
