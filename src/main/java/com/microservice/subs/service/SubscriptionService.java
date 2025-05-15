package com.microservice.subs.service;

import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.SubscriptionResponseDto;
import com.microservice.subs.dto.TopSubscriptionDto;
import com.microservice.subs.mapper.SubscriptionMapper;
import com.microservice.subs.model.Subscription;
import com.microservice.subs.model.User;
import com.microservice.subs.repository.SubscriptionRepository;
import com.microservice.subs.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final UserRepository userRepository;
    private final SubscriptionMapper mapper;

    public SubscriptionResponseDto addSubscription(Long userId, SubscriptionRequestDto requestDto) {
        log.info("Adding subscription for user ID: {}, service: {}", userId, requestDto.serviceName());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });
        Subscription subscription = mapper.toEntity(requestDto);
        subscription.setUser(user);
        Subscription savedSubscription = repository.save(subscription);
        log.info("Subscription added with ID: {}", savedSubscription.getId());
        return mapper.toResponseDto(savedSubscription);
    }

    public List<SubscriptionResponseDto> getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        List<Subscription> subscriptions = repository.findByUserId(userId);
        return subscriptions.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Deleting subscription ID: {} for user ID: {}", subscriptionId, userId);
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        Subscription subscription = repository.findById(subscriptionId)
                .orElseThrow(() -> {
                    log.error("Subscription not found with ID: {}", subscriptionId);
                    return new EntityNotFoundException("Subscription not found with ID: " + subscriptionId);
                });
        if (!subscription.getUser().getId().equals(userId)) {
            log.error("Subscription ID: {} does not belong to user ID: {}", subscriptionId, userId);
            throw new IllegalArgumentException("Subscription does not belong to user");
        }
        repository.deleteById(subscriptionId);
        log.info("Subscription deleted with ID: {}", subscriptionId);
    }

    public List<TopSubscriptionDto> getTopSubscriptions() {
        log.info("Fetching top 3 popular subscriptions");
        List<Object[]> results = repository.findTop3PopularSubscriptions();
        return results.stream()
                .map(result -> TopSubscriptionDto.builder()
                        .serviceName((String) result[0])
                        .count((Long) result[1])
                        .build())
                .collect(Collectors.toList());
    }
}
