package com.microservice.subs.service;

import com.microservice.subs.dto.UserRequestDto;
import com.microservice.subs.dto.UserResponseDto;
import com.microservice.subs.mapper.UserMapper;
import com.microservice.subs.model.User;
import com.microservice.subs.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    private UserRequestDto requestDto;
    private User user;
    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = UserRequestDto.builder()
                .email("test@example.com")
                .name("Test User")
                .build();
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        responseDto = UserResponseDto.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .build();
    }

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(userRepository).save(user);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    void createUser_emailExists_throwsException() {
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(1L));
        verify(userMapper, never()).toResponseDto(any());
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(any());
    }
}
