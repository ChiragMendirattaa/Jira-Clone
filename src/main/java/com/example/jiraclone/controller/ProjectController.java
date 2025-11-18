package com.example.jiraclone.controller;

import com.example.jiraclone.dto.AddMemberRequestDTO;
import com.example.jiraclone.dto.IssueResponseDTO;
import com.example.jiraclone.dto.ProjectRequestDTO;
import com.example.jiraclone.dto.ProjectResponseDTO;
import com.example.jiraclone.service.IssueService;
import com.example.jiraclone.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final IssueService issueService;

    public ProjectController(ProjectService projectService, IssueService issueService) {
        this.projectService = projectService;
        this.issueService = issueService;
    }

    /**
     * Creates a new project.
     * POST /api/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO projectRequestDTO
    ) {
        ProjectResponseDTO createdProject = projectService.createProject(projectRequestDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    /**
     * Gets all projects for the currently authenticated user.
     * GET /api/projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsForCurrentUser() {
        List<ProjectResponseDTO> projects = projectService.getProjectsForCurrentUser();
        return ResponseEntity.ok(projects);
    }

    /**
     * Gets a single project by its ID.
     * GET /api/projects/{projectId}
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @PathVariable Long projectId
    ) {
        ProjectResponseDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    /**
     * Updates a project's details.
     * PUT /api/projects/{projectId}
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDTO projectRequestDTO
    ) {
        ProjectResponseDTO updatedProject = projectService.updateProject(projectId, projectRequestDTO);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Deletes a project by its ID.
     * DELETE /api/projects/{projectId}
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    // --- Nested Issue Endpoints ---

    /**
     * Gets all issues for a specific project.
     * GET /api/projects/{projectId}/issues
     */
    @GetMapping("/{projectId}/issues")
    public ResponseEntity<List<IssueResponseDTO>> getIssuesForProject(
            @PathVariable Long projectId
    ) {
        List<IssueResponseDTO> issues = issueService.getIssuesForProject(projectId);
        return ResponseEntity.ok(issues);
    }

    // --- Nested Member Endpoints ---

    /**
     * Adds a member to a project.
     * POST /api/projects/{projectId}/members
     */
    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectResponseDTO> addMemberToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody AddMemberRequestDTO addMemberRequest
    ) {
        ProjectResponseDTO updatedProject = projectService.addMemberToProject(projectId, addMemberRequest);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Removes a member from a project.
     * DELETE /api/projects/{projectId}/members/{userId}
     */
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ProjectResponseDTO> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        ProjectResponseDTO updatedProject = projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }
}