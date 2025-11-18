package com.example.jiraclone.dto;

import com.example.jiraclone.enums.IssuePriority;
import com.example.jiraclone.enums.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIssueRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Issue type is required")
    private IssueType type;

    @NotNull(message = "Priority is required")
    private IssuePriority priority;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Reporter ID is required")
    private Long reporterId;

    private Long assigneeId;

    private Long parentIssueId;
}