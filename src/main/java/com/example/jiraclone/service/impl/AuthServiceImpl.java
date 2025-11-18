package com.example.jiraclone.service.impl;

import com.example.jiraclone.dto.AuthResponseDTO;
import com.example.jiraclone.dto.LoginRequestDTO;
import com.example.jiraclone.dto.UserRegistrationRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;
import com.example.jiraclone.entity.User;
import com.example.jiraclone.exception.UserAlreadyExistsException;
import com.example.jiraclone.repository.UserRepository;
import com.example.jiraclone.security.JwtUtil;
import com.example.jiraclone.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationRequestDTO registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        User savedUser = userRepository.save(newUser);

        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO loginRequest) {

        // 1. Let Spring Security authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. Set it in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. --- THIS IS THE FIX ---
        // Pass the 'authentication' object, not the 'user' object
        String token = jwtUtil.generateToken(authentication);

        // 4. Get the User principal
        User user = (User) authentication.getPrincipal();

        // 5. Map to DTO
        UserResponseDTO userResponse = mapToUserResponseDTO(user);

        // 6. Return response
        return new AuthResponseDTO(token, userResponse);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}