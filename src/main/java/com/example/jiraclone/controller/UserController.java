package com.example.jiraclone.controller;

import com.example.jiraclone.dto.ChangePasswordRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;
import com.example.jiraclone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Searches for users by username or email.
     * GET /api/users/search?query=john
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
            @RequestParam String query
    ) {
        // Prevent searching for empty strings
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<UserResponseDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO request
    ) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}