package com.example.jiraclone.dto;

import lombok.Getter;
import lombok.Setter;

// A lightweight DTO for nesting in other responses
@Getter
@Setter
public class UserSummaryDTO {
    private Long id;
    private String username;
}