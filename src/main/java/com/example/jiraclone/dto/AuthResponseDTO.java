package com.example.jiraclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // A simple constructor for the service
public class AuthResponseDTO {

    private String token; // The JWT
    private UserResponseDTO user; // Details of the logged-in user
}