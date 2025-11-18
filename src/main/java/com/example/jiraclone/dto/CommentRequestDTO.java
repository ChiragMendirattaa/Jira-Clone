package com.example.jiraclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    @NotBlank(message = "Comment body cannot be empty")
    private String body;

    @NotNull(message = "Issue ID is required")
    private Long issueId;

    @NotNull(message = "Author ID is required")
    private Long authorId;
}