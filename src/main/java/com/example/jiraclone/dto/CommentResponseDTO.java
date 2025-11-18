package com.example.jiraclone.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDTO {

    private Long id;
    private String body;
    private LocalDateTime createdAt;

    // We use the summary DTO again
    private UserSummaryDTO author;
    private Long issueId;
}