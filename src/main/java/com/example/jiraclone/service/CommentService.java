package com.example.jiraclone.service;

import com.example.jiraclone.dto.CommentRequestDTO;
import com.example.jiraclone.dto.CommentResponseDTO;

import java.util.List;

public interface CommentService {

    CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO);

    List<CommentResponseDTO> getCommentsForIssue(Long issueId);

    void deleteComment(Long commentId);

    /**
     * Updates the body of an existing comment.
     *
     * @param commentId         The ID of the comment to update.
     * @param commentRequestDTO DTO containing the new body.
     * @return DTO of the updated comment.
     */
    CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO); // <-- ADD THIS
}