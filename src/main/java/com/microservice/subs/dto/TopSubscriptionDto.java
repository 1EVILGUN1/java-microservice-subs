package com.microservice.subs.dto;

import lombok.Builder;

@Builder
public record TopSubscriptionDto(
        String serviceName,
        Long count
) {}
