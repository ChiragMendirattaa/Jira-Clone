package com.example.jiraclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank(message = "Email or username is required")
    private String email; // Or username, your service can handle both

    @NotBlank(message = "Password is required")
    private String password;
}