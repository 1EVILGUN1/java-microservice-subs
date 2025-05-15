package com.microservice.subs.controller;

import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.SubscriptionResponseDto;
import com.microservice.subs.dto.TopSubscriptionDto;
import com.microservice.subs.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/subscriptions")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto addSubscription(@PathVariable Long userId,
                                                   @Valid @RequestBody SubscriptionRequestDto requestDto) {
        return subscriptionService.addSubscription(userId, requestDto);
    }

    @GetMapping
    public List<SubscriptionResponseDto> getUserSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }

    @DeleteMapping("/{subscriptionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        subscriptionService.deleteSubscription(userId, subscriptionId);
    }

    @GetMapping("/top")
    public List<TopSubscriptionDto> getTopSubscriptions() {
        return subscriptionService.getTopSubscriptions();
    }
}
