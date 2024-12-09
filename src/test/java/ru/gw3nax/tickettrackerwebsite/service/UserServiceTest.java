package ru.gw3nax.tickettrackerwebsite.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gw3nax.tickettrackerwebsite.dto.request.UserRequest;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotAuthenticatedException;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotFoundException;
import ru.gw3nax.tickettrackerwebsite.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConversionService conversionService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private UserRequest userRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password123");
    }

    @Test
    void createUser_Success() {
        // Mock setup
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(conversionService.convert(userRequest, UserEntity.class)).thenReturn(userEntity);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.createUser(userRequest);

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(userEntity);

        assertEquals("encodedPassword", userEntity.getPassword());
        assertEquals("USER", userEntity.getRole());
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.createUser(userRequest));
        assertEquals("Пользователь с данной test@example.com уже зарегистрирован", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getCurrentUser_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        UserEntity result = userService.getCurrentUser();
        assertEquals(userEntity, result);
    }

    @Test
    void getCurrentUser_NotAuthenticated_ThrowsException() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        Exception exception = assertThrows(UserNotAuthenticatedException.class, () -> userService.getCurrentUser());
        assertEquals("Пользователь не авторизован", exception.getMessage());
    }

    @Test
    void getCurrentUser_UserNotFound_ThrowsException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
        assertEquals("Пользователь не найден: test@example.com", exception.getMessage());
    }
}
