package com.example.backend.dto;

import com.example.backend.enums.RoleName;

public record CreateUserDto(
        String name,
        String password,
        RoleName role
) {
}
