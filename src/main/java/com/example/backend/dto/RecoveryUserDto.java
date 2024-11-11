package com.example.backend.dto;

import com.example.backend.entities.Role;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String name,
        List<Role> roles
) {
}
