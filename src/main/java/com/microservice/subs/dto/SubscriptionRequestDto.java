package com.microservice.subs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SubscriptionRequestDto(
        @NotBlank(message = "Service name is required")
        @Size(min = 2, max = 100, message = "Service name is required")
        String serviceName
) {}
