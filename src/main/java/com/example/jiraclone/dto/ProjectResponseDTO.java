package com.example.jiraclone.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String projectKey;
    private Long ownerId;

    // We use the summary DTO here to prevent loops and excess data
    private Set<UserSummaryDTO> members;
}