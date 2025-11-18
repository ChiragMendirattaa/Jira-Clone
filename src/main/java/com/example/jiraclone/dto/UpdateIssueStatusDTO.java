package com.example.jiraclone.dto; // (Your package name)

import com.example.jiraclone.enums.IssueStatus; // <-- IMPORT THIS
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIssueStatusDTO {

    @NotNull(message = "Status cannot be null")
    private IssueStatus status; // <-- REVERTED

    // private Long statusId; // <-- REMOVE THIS
}