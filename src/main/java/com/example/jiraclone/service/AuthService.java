package com.example.jiraclone.service;

import com.example.jiraclone.dto.AuthResponseDTO;
import com.example.jiraclone.dto.LoginRequestDTO;
import com.example.jiraclone.dto.UserRegistrationRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;

// Note: No @Service annotation here.
public interface AuthService {

    /**
     * Registers a new user in the system.
     * @param registrationRequest DTO containing user registration details
     * @return UserResponseDTO of the newly created user
     * @throws com.example.jiraclone.exception.UserAlreadyExistsException if username or email is taken
     */
    UserResponseDTO registerUser(UserRegistrationRequestDTO registrationRequest);

    /**
     * Authenticates a user and returns a JWT.
     * @param loginRequest DTO containing user login credentials
     * @return AuthResponseDTO containing the JWT and user details
     * @throws org.springframework.security.core.AuthenticationException if credentials are invalid
     */
    AuthResponseDTO loginUser(LoginRequestDTO loginRequest);

    // We've added loginUser here as well for the future.
}