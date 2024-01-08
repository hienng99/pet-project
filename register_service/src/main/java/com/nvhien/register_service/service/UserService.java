package com.nvhien.register_service.service;

import com.nvhien.register_service.model.entity.User;
import com.nvhien.register_service.model.dto.UserRequest;
import com.nvhien.register_service.repository.UserRepository;
import com.nvhien.register_service.util.Result;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result create(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return Result.USERNAME_ALREADY_EXIST;
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return Result.EMAIL_ALREADY_EXIST;
        }

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .build();

        userRepository.save(user);
        return Result.SUCCESS;
    }
}
