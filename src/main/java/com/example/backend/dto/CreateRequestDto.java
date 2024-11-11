package com.example.backend.dto;

import com.example.backend.entities.User;

import java.util.Date;

public record CreateRequestDto(
        String title,
        String description,
        Float price,
        Date maxSignatureDate
) {
}
