package com.example.jiraclone.service.impl;

import com.example.jiraclone.dto.ChangePasswordRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;
import com.example.jiraclone.entity.User;
import com.example.jiraclone.exception.UnauthorizedAccessException;
import com.example.jiraclone.repository.UserRepository;
import com.example.jiraclone.service.SecurityService;
import com.example.jiraclone.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService; // <-- INJECT
    private final PasswordEncoder passwordEncoder; // <-- INJECT

    public UserServiceImpl(UserRepository userRepository,
                           SecurityService securityService,
                           PasswordEncoder passwordEncoder) { // <-- INJECT
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(String query) {

        // 1. Use the repository method to search by both username and email
        List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);

        // 2. Map the results to DTOs
        return users.stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    // --- ADD THIS NEW METHOD ---
    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        // 1. Get the current user
        User currentUser = securityService.getAuthenticatedUser();

        // 2. Check if the old password is correct
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new UnauthorizedAccessException("Incorrect old password");
        }

        // 3. Encode and set the new password
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 4. Save the user
        userRepository.save(currentUser);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}