package com.example.backend.dto;

import com.example.backend.entities.Request;

import java.util.Date;

public record RequestResponseDto(
        Long id,
        String title,
        String description,
        Float price,
        String requesterName,
        String approverName,
        String signature,
        Date signatureDate,
        Date createAt,
        Date maxSignatureDate

) {

    public static RequestResponseDto fromEntity(Request request) {
        return new RequestResponseDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getRequester().getName(),
                request.getApprover() != null ? request.getApprover().getName() : null,
                request.getSignature() != null ? request.getSignature() : null,
                request.getSignatureDate() != null ? request.getSignatureDate() : null,
                request.getCreateAt(),
                request.getMaxSignatureDate()
        );
    }


}
