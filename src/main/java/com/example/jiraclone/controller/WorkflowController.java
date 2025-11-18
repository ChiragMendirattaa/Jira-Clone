package com.example.jiraclone.controller;

import com.example.jiraclone.dto.WorkflowStatusDTO;
import com.example.jiraclone.entity.Project;
import com.example.jiraclone.entity.WorkflowStatus;
import com.example.jiraclone.exception.ProjectNotFoundException;
import com.example.jiraclone.repository.ProjectRepository;
import com.example.jiraclone.repository.WorkflowStatusRepository;
import com.example.jiraclone.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/workflow")
public class WorkflowController {

    private final WorkflowStatusRepository workflowStatusRepository;
    private final ProjectRepository projectRepository;
    private final SecurityService securityService;

    public WorkflowController(WorkflowStatusRepository workflowStatusRepository,
                              ProjectRepository projectRepository,
                              SecurityService securityService) {
        this.workflowStatusRepository = workflowStatusRepository;
        this.projectRepository = projectRepository;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<List<WorkflowStatusDTO>> getWorkflow(@PathVariable Long projectId) {
        securityService.checkUserIsMemberOfProject(projectId);
        List<WorkflowStatus> statuses = workflowStatusRepository.findByProjectIdOrderByOrderAsc(projectId);
        List<WorkflowStatusDTO> dtos = statuses.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<WorkflowStatusDTO> createStatus(@PathVariable Long projectId,
                                                          @RequestBody WorkflowStatusDTO statusDTO) {
        securityService.checkUserIsProjectOwner(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        WorkflowStatus newStatus = new WorkflowStatus();
        newStatus.setName(statusDTO.getName());
        newStatus.setOrder(statusDTO.getOrder());
        newStatus.setProject(project);

        WorkflowStatus savedStatus = workflowStatusRepository.save(newStatus);
        return ResponseEntity.ok(mapToDTO(savedStatus));
    }

    // ... (You would also add @PutMapping, @DeleteMapping here)

    private WorkflowStatusDTO mapToDTO(WorkflowStatus status) {
        WorkflowStatusDTO dto = new WorkflowStatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setOrder(status.getOrder());
        return dto;
    }
}