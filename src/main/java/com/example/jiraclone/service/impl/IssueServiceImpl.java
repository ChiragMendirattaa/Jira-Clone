package com.example.jiraclone.service.impl; // (Or your package name)

import com.example.jiraclone.dto.CreateIssueRequestDTO;
import com.example.jiraclone.dto.IssueResponseDTO;
import com.example.jiraclone.dto.SubtaskResponseDTO;
import com.example.jiraclone.dto.UpdateIssueRequestDTO;
import com.example.jiraclone.dto.UpdateIssueStatusDTO;
// import com.example.jiraclone.dto.WorkflowStatusDTO; // <-- REMOVED
import com.example.jiraclone.entity.Issue;
import com.example.jiraclone.entity.Project;
import com.example.jiraclone.entity.User;
// import com.example.jiraclone.entity.WorkflowStatus; // <-- REMOVED
import com.example.jiraclone.enums.IssueStatus; // <-- RE-IMPORTED
import com.example.jiraclone.exception.IssueNotFoundException;
import com.example.jiraclone.exception.ResourceNotFoundException;
import com.example.jiraclone.exception.UnauthorizedAccessException;
import com.example.jiraclone.repository.IssueRepository;
import com.example.jiraclone.repository.ProjectRepository;
import com.example.jiraclone.repository.UserRepository;
// import com.example.jiraclone.repository.WorkflowStatusRepository; // <-- REMOVED
import com.example.jiraclone.service.IssueService;
import com.example.jiraclone.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    // private final WorkflowStatusRepository workflowStatusRepository; // <-- REMOVED

    public IssueServiceImpl(IssueRepository issueRepository,
                            ProjectRepository projectRepository,
                            UserRepository userRepository,
                            SecurityService securityService
            /*, WorkflowStatusRepository workflowStatusRepository */) { // <-- REMOVED from constructor
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
        // this.workflowStatusRepository = workflowStatusRepository; // <-- REMOVED
    }

    @Override
    @Transactional
    public IssueResponseDTO createIssue(CreateIssueRequestDTO createIssueRequest) {
        Long projectId = createIssueRequest.getProjectId();
        securityService.checkUserIsMemberOfProject(projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        User reporter = securityService.getAuthenticatedUser();

        User assignee = null;
        if (createIssueRequest.getAssigneeId() != null) {
            assignee = userRepository.findById(createIssueRequest.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", createIssueRequest.getAssigneeId()));
        }

        Issue parentIssue = null;
        if (createIssueRequest.getParentIssueId() != null) {
            parentIssue = issueRepository.findById(createIssueRequest.getParentIssueId())
                    .orElseThrow(() -> new IssueNotFoundException("Parent issue not found with id: " + createIssueRequest.getParentIssueId()));
        }

        Issue issue = new Issue();
        issue.setTitle(createIssueRequest.getTitle());
        issue.setDescription(createIssueRequest.getDescription());
        issue.setType(createIssueRequest.getType());
        issue.setPriority(createIssueRequest.getPriority());
        issue.setStatus(IssueStatus.TO_DO); // <-- Reverted to default enum
        issue.setProject(project);
        issue.setReporter(reporter);
        issue.setAssignee(assignee);
        issue.setParentIssue(parentIssue);

        Issue savedIssue = issueRepository.save(issue);
        return mapToIssueResponseDTO(savedIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssuesForProject(Long projectId) {
        securityService.checkUserIsMemberOfProject(projectId);
        List<Issue> issues = issueRepository.findByProjectId(projectId);
        return issues.stream()
                .map(this::mapToIssueResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IssueResponseDTO updateIssueStatus(Long issueId, UpdateIssueStatusDTO statusDTO) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));

        securityService.checkUserIsMemberOfProject(issue.getProject().getId());

        // --- REVERTED STATUS LOGIC ---
        issue.setStatus(statusDTO.getStatus()); // <-- Set the enum from the DTO
        // --- END REVERT ---

        Issue updatedIssue = issueRepository.save(issue);
        return mapToIssueResponseDTO(updatedIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public IssueResponseDTO getIssueById(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));
        securityService.checkUserIsMemberOfProject(issue.getProject().getId());
        return mapToIssueResponseDTO(issue);
    }

    @Override
    @Transactional
    public IssueResponseDTO updateIssue(Long issueId, UpdateIssueRequestDTO updateRequest) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));
        securityService.checkUserIsMemberOfProject(issue.getProject().getId());
        User assignee = null;
        if (updateRequest.getAssigneeId() != null) {
            assignee = userRepository.findById(updateRequest.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User (assignee)", "id", updateRequest.getAssigneeId()));
        }
        issue.setTitle(updateRequest.getTitle());
        issue.setDescription(updateRequest.getDescription());
        issue.setType(updateRequest.getType());
        issue.setPriority(updateRequest.getPriority());
        issue.setAssignee(assignee);
        Issue updatedIssue = issueRepository.save(issue);
        return mapToIssueResponseDTO(updatedIssue);
    }

    @Override
    @Transactional
    public void deleteIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));
        securityService.checkUserIsMemberOfProject(issue.getProject().getId());
        issueRepository.delete(issue);
    }

    /**
     * Helper to map an Issue entity to its full response DTO.
     */
    private IssueResponseDTO mapToIssueResponseDTO(Issue issue) {
        IssueResponseDTO dto = new IssueResponseDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setType(issue.getType());
        dto.setPriority(issue.getPriority());
        dto.setStatus(issue.getStatus()); // <-- Reverted to enum

        dto.setProjectId(issue.getProject().getId());
        dto.setProjectKey(issue.getProject().getProjectKey());
        dto.setProjectName(issue.getProject().getName());

        dto.setReporterId(issue.getReporter().getId());
        dto.setReporterName(issue.getReporter().getUsername());

        if (issue.getAssignee() != null) {
            dto.setAssigneeId(issue.getAssignee().getId());
            dto.setAssigneeName(issue.getAssignee().getUsername());
        }

        if (issue.getParentIssue() != null) {
            dto.setParentIssueId(issue.getParentIssue().getId());
        }

        if (issue.getSubtasks() != null) {
            dto.setSubtasks(
                    issue.getSubtasks().stream()
                            .map(this::mapToSubtaskResponseDTO)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    /**
     * Helper to map an Issue entity to a lightweight Subtask DTO.
     */
    private SubtaskResponseDTO mapToSubtaskResponseDTO(Issue issue) {
        SubtaskResponseDTO dto = new SubtaskResponseDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setType(issue.getType());
        dto.setStatus(issue.getStatus()); // <-- Reverted to enum
        dto.setPriority(issue.getPriority());

        if (issue.getAssignee() != null) {
            dto.setAssigneeId(issue.getAssignee().getId());
            dto.setAssigneeName(issue.getAssignee().getUsername());
        }
        return dto;
    }

    // --- REMOVED mapToWorkflowStatusDTO helper method ---
}