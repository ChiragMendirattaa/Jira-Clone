package com.example.jiraclone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    // We can add other safe fields here later, like 'jobTitle' or 'avatarUrl'
}