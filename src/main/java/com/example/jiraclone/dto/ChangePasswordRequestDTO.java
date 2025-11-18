package com.example.jiraclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;
}