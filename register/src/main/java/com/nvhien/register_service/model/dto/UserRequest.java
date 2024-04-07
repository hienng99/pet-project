package com.nvhien.register_service.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
}
