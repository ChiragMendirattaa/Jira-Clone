package com.example.jiraclone.controller; // (Or your package name)

import com.example.jiraclone.dto.CommentRequestDTO;
import com.example.jiraclone.dto.CommentResponseDTO;
import com.example.jiraclone.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base path for all API endpoints
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Creates a new comment on an issue.
     * POST /api/issues/{issueId}/comments
     */
    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO
    ) {
        // Ensure the ID in the path matches the ID in the body
        if (!issueId.equals(commentRequestDTO.getIssueId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CommentResponseDTO createdComment = commentService.createComment(commentRequestDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * Gets all comments for an issue.
     * GET /api/issues/{issueId}/comments
     */
    @GetMapping("/issues/{issueId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsForIssue(
            @PathVariable Long issueId
    ) {
        List<CommentResponseDTO> comments = commentService.getCommentsForIssue(issueId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Updates an existing comment.
     * PUT /api/comments/{commentId}
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO
    ) {
        CommentResponseDTO updatedComment = commentService.updateComment(commentId, commentRequestDTO);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * Deletes a comment by its ID.
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}