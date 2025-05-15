package com.microservice.subs.service;

import com.microservice.subs.dto.UserRequestDto;
import com.microservice.subs.dto.UserResponseDto;
import com.microservice.subs.mapper.UserMapper;
import com.microservice.subs.model.User;
import com.microservice.subs.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserResponseDto createUser(UserRequestDto requestDto) {
        log.info("Creating user with email: {}", requestDto.email());
        if (repository.existsByEmail(requestDto.email())) {
            log.error("Email already exists: {}", requestDto.email());
            throw new IllegalArgumentException("Email already exists");
        }
        User user = mapper.toEntity(requestDto);
        User savedUser = repository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return mapper.toResponseDto(savedUser);
    }

    public UserResponseDto getUser(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
        return mapper.toResponseDto(user);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        log.info("Updating user with ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
        if (!user.getEmail().equals(requestDto.email()) && repository.existsByEmail(requestDto.email())) {
            log.error("Email already exists: {}", requestDto.email());
            throw new IllegalArgumentException("Email already exists");
        }
        mapper.updateEntityFromDto(requestDto, user);
        User updatedUser = repository.save(user);
        log.info("User updated with ID: {}", updatedUser.getId());
        return mapper.toResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!repository.existsById(id)) {
            log.error("User not found with ID: {}", id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        repository.deleteById(id);
        log.info("User deleted with ID: {}", id);
    }
}
