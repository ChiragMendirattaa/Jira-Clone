package com.example.jiraclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    private String name;

    @NotBlank(message = "Project key is required")
    @Size(min = 2, max = 5, message = "Key must be between 2 and 5 characters")
    private String projectKey; // e.g., "APP"
}