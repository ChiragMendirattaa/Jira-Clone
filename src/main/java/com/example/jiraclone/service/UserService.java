package com.example.jiraclone.service;

import com.example.jiraclone.dto.ChangePasswordRequestDTO;
import com.example.jiraclone.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> searchUsers(String query);

    /**
     * Changes the password for the currently authenticated user.
     *
     * @param request DTO containing old and new passwords.
     */
    void changePassword(ChangePasswordRequestDTO request); // <-- ADD THIS
}