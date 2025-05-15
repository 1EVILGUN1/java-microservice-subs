package com.microservice.subs.service;

import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.SubscriptionResponseDto;
import com.microservice.subs.mapper.SubscriptionMapper;
import com.microservice.subs.model.Subscription;
import com.microservice.subs.model.User;
import com.microservice.subs.repository.SubscriptionRepository;
import com.microservice.subs.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SubscriptionMapper subscriptionMapper;
    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private Subscription subscription;
    private SubscriptionRequestDto requestDto;
    private SubscriptionResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setServiceName("YouTube Premium");
        requestDto = SubscriptionRequestDto.builder()
                .serviceName("YouTube Premium")
                .build();
        responseDto = SubscriptionResponseDto.builder()
                .id(1L)
                .userId(1L)
                .serviceName("YouTube Premium")
                .build();
    }

    @Test
    void addSubscription_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionMapper.toEntity(requestDto)).thenReturn(subscription);
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);
        when(subscriptionMapper.toResponseDto(subscription)).thenReturn(responseDto);

        SubscriptionResponseDto result = subscriptionService.addSubscription(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void addSubscription_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> subscriptionService.addSubscription(1L, requestDto));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void getUserSubscriptions_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findByUserId(1L)).thenReturn(Collections.singletonList(subscription));
        when(subscriptionMapper.toResponseDto(subscription)).thenReturn(responseDto);

        var result = subscriptionService.getUserSubscriptions(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(subscriptionRepository).findByUserId(1L);
    }

    @Test
    void deleteSubscription_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        subscriptionService.deleteSubscription(1L, 1L);

        verify(subscriptionRepository).deleteById(1L);
    }

    @Test
    void deleteSubscription_wrongUser_throwsException() {
        User differentUser = new User();
        differentUser.setId(2L);
        subscription.setUser(differentUser);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        assertThrows(IllegalArgumentException.class, () -> subscriptionService.deleteSubscription(1L, 1L));
        verify(subscriptionRepository, never()).deleteById(any());
    }
}
