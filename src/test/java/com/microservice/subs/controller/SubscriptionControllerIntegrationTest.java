package com.microservice.subs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.subs.dto.SubscriptionRequestDto;
import com.microservice.subs.dto.UserRequestDto;
import com.microservice.subs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubscriptionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        // Generate unique email for each test
        String uniqueEmail = "test-" + UUID.randomUUID() + "@example.com";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(uniqueEmail)
                .name("Test User")
                .build();
        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        userId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void addSubscription_success() throws Exception {
        SubscriptionRequestDto requestDto = SubscriptionRequestDto.builder()
                .serviceName("YouTube Premium")
                .build();

        mockMvc.perform(post("/users/{userId}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceName").value("YouTube Premium"))
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    void addSubscription_invalidServiceName_throwsValidationError() throws Exception {
        SubscriptionRequestDto requestDto = SubscriptionRequestDto.builder()
                .serviceName("")
                .build();

        mockMvc.perform(post("/users/{userId}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.serviceName").value("Service name is required"));
    }

    @Test
    void getTopSubscriptions_emptyList_success() throws Exception {
        mockMvc.perform(get("/users/{userId}/subscriptions/top", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
