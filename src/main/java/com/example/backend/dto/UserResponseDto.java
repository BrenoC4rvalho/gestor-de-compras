package com.example.backend.dto;

import com.example.backend.entities.User;
import com.example.backend.enums.RoleName;

public record UserResponseDto(
        Long id,
        String name,
        RoleName role
) {
    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getRoles().isEmpty() ? null : user.getRoles().get(0).getName()
        );

    }
}
