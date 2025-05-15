package com.microservice.subs.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SubscriptionResponseDto(
        Long id,
        Long userId,
        String serviceName,
        LocalDateTime createdAt
) {}
