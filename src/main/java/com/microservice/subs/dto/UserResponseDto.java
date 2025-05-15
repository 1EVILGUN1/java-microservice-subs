package com.microservice.subs.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
