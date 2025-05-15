package com.microservice.subs.mapper;

import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.SubscriptionResponseDto;
import com.microservice.subs.model.Subscription;
import com.microservice.subs.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T21:07:15+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public Subscription toEntity(SubscriptionRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Subscription subscription = new Subscription();

        subscription.setServiceName( dto.serviceName() );

        return subscription;
    }

    @Override
    public SubscriptionResponseDto toResponseDto(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }

        SubscriptionResponseDto.SubscriptionResponseDtoBuilder subscriptionResponseDto = SubscriptionResponseDto.builder();

        subscriptionResponseDto.userId( subscriptionUserId( subscription ) );
        subscriptionResponseDto.id( subscription.getId() );
        subscriptionResponseDto.serviceName( subscription.getServiceName() );
        subscriptionResponseDto.createdAt( subscription.getCreatedAt() );

        return subscriptionResponseDto.build();
    }

    private Long subscriptionUserId(Subscription subscription) {
        User user = subscription.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
