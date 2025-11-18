package com.example.jiraclone.controller;

import com.example.jiraclone.dto.AuthResponseDTO; // Import this
import com.example.jiraclone.dto.LoginRequestDTO; // Import this
import com.example.jiraclone.dto.UserRegistrationRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;
import com.example.jiraclone.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRegistrationRequestDTO registrationRequest
    ) {
        UserResponseDTO newUser = authService.registerUser(registrationRequest);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint for user login.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(
            @Valid @RequestBody LoginRequestDTO loginRequest
    ) {
        // The service will handle authentication
        // If credentials are bad, an AuthenticationException will be thrown,
        // and Spring Security will handle the 401 Unauthorized response.
        AuthResponseDTO authResponse = authService.loginUser(loginRequest);

        // If successful, return 200 OK with the token
        return ResponseEntity.ok(authResponse);
    }
}