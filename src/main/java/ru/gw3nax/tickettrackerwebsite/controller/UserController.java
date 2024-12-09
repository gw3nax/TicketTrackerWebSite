package ru.gw3nax.tickettrackerwebsite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.tickettrackerwebsite.dto.request.UserRequest;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;
import ru.gw3nax.tickettrackerwebsite.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user registration, login, and management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Login page", description = "Returns the login page")
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @Operation(summary = "Registration page", description = "Returns the user registration page")
    @GetMapping("/register")
    public String registerUser() {
        return "registration_form";
    }

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirects to the login page after registration"),
            @ApiResponse(responseCode = "400", description = "Invalid user input")
    })
    @PostMapping("/register")
    public String registerUser(@RequestBody UserRequest user) {
        userService.createUser(user);
        return "redirect:/login";
    }

    @Operation(summary = "User home page", description = "Returns the home page for the user")
    @GetMapping("/home")
    public String getUserHomePage() {
        return "home_page";
    }
}
