package com.microservice.subs.mapper;

import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.SubscriptionResponseDto;
import com.microservice.subs.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Subscription toEntity(SubscriptionRequestDto dto);

    @Mapping(source = "user.id", target = "userId")
    SubscriptionResponseDto toResponseDto(Subscription subscription);
}
