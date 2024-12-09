package ru.gw3nax.tickettrackerwebsite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.gw3nax.tickettrackerwebsite.dto.request.UserRequest;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;
import ru.gw3nax.tickettrackerwebsite.exception.exceptions.UserNotFoundException;
import ru.gw3nax.tickettrackerwebsite.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;


    public void createUser(UserRequest userRequest){
        log.info("Creating new user: {}", userRequest);
        if (userRepository.existsByEmail(userRequest.getEmail())){
            throw new UsernameNotFoundException("Пользователь с данной " +  userRequest.getEmail() + " уже зарегистрирован");
        }
        var user = conversionService.convert(userRequest, UserEntity.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        log.info("Created user: " + user.getEmail());
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException("Пользователь не найден");
    }
}
