package com.example.jiraclone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}