package com.example.jiraclone.dto;

import com.example.jiraclone.enums.IssuePriority;
import com.example.jiraclone.enums.IssueStatus;
import com.example.jiraclone.enums.IssueType;
import lombok.Getter;
import lombok.Setter;

/**
 * A lightweight DTO for representing a sub-task or child issue.
 */
@Getter
@Setter
public class SubtaskResponseDTO {
    private Long id;
    private String title;
    private IssueType type;
    private IssueStatus status;
    private IssuePriority priority;
    private Long assigneeId;
    private String assigneeName;
}