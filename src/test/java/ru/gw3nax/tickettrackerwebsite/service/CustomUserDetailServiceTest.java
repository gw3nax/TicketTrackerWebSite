package ru.gw3nax.tickettrackerwebsite.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotFoundException;
import ru.gw3nax.tickettrackerwebsite.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    CustomUserDetailServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .password("encryptedPassword")
                .role("ADMIN,USER")
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = customUserDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encryptedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> customUserDetailService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void loadUserByUsername_UserWithoutRole_ReturnsDefaultUserRole() {
        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .password("encryptedPassword")
                .role(null)
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = customUserDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
}
