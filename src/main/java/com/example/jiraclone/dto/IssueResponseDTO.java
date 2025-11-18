package com.example.jiraclone.dto;

import com.example.jiraclone.enums.IssuePriority;
import com.example.jiraclone.enums.IssueStatus;
import com.example.jiraclone.enums.IssueType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class IssueResponseDTO {

    private Long id;
    private String title;
    private String description;

    // We can send the enums directly
    private IssueType type;
    private IssueStatus status;
    private IssuePriority priority;

    // --- Flattened data from related entities ---

    // Project details
    private Long projectId;
    private String projectKey; // e.g., "APP"
    private String projectName;

    // Reporter details
    private Long reporterId;
    private String reporterName;

    // Assignee details
    private Long assigneeId; // Can be null
    private String assigneeName; // Can be null

    private Long parentIssueId;
    private Set<SubtaskResponseDTO> subtasks;
}