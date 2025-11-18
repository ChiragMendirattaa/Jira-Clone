package com.example.jiraclone.dto;

import com.example.jiraclone.enums.IssuePriority;
import com.example.jiraclone.enums.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIssueRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Issue type is required")
    private IssueType type;

    @NotNull(message = "Priority is required")
    private IssuePriority priority;

    // The ID of the user to assign the issue to.
    // Can be null to unassign.
    private Long assigneeId;
}