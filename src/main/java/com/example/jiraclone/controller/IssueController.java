package com.example.jiraclone.controller;

import com.example.jiraclone.dto.CreateIssueRequestDTO;
import com.example.jiraclone.dto.IssueResponseDTO;
import com.example.jiraclone.dto.UpdateIssueRequestDTO;
import com.example.jiraclone.dto.UpdateIssueStatusDTO;
import com.example.jiraclone.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    // The CommentService is no longer injected here
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * Creates a new issue.
     * POST /api/issues
     */
    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(
            @Valid @RequestBody CreateIssueRequestDTO createIssueRequest
    ) {
        IssueResponseDTO createdIssue = issueService.createIssue(createIssueRequest);
        return new ResponseEntity<>(createdIssue, HttpStatus.CREATED);
    }

    /**
     * Gets a single issue by its ID.
     * GET /api/issues/{issueId}
     */
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponseDTO> getIssueById(
            @PathVariable Long issueId
    ) {
        IssueResponseDTO issue = issueService.getIssueById(issueId);
        return ResponseEntity.ok(issue);
    }

    /**
     * Updates an issue's details (title, description, assignee, etc.).
     * PUT /api/issues/{issueId}
     */
    @PutMapping("/{issueId}")
    public ResponseEntity<IssueResponseDTO> updateIssue(
            @PathVariable Long issueId,
            @Valid @RequestBody UpdateIssueRequestDTO updateRequest
    ) {
        IssueResponseDTO updatedIssue = issueService.updateIssue(issueId, updateRequest);
        return ResponseEntity.ok(updatedIssue);
    }

    /**
     * Updates an issue's status (e.g., for drag-and-drop).
     * PATCH /api/issues/{issueId}/status
     */
    @PatchMapping("/{issueId}/status")
    public ResponseEntity<IssueResponseDTO> updateIssueStatus(
            @PathVariable Long issueId,
            @Valid @RequestBody UpdateIssueStatusDTO statusDTO
    ) {
        IssueResponseDTO updatedIssue = issueService.updateIssueStatus(issueId, statusDTO);
        return ResponseEntity.ok(updatedIssue);
    }

    /**
     * Deletes an issue by its ID.
     * DELETE /api/issues/{issueId}
     */
    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ResponseEntity.noContent().build();
    }

    // The comment endpoints (GET and POST) have been moved to CommentController
}