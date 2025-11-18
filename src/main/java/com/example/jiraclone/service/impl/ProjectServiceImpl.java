package com.example.jiraclone.service.impl; // (Or your package name)

import com.example.jiraclone.dto.AddMemberRequestDTO;
import com.example.jiraclone.dto.ProjectRequestDTO;
import com.example.jiraclone.dto.ProjectResponseDTO;
import com.example.jiraclone.dto.UserSummaryDTO;
import com.example.jiraclone.entity.Project;
import com.example.jiraclone.entity.User;
import com.example.jiraclone.exception.ProjectNotFoundException;
import com.example.jiraclone.exception.ResourceNotFoundException;
import com.example.jiraclone.exception.UnauthorizedAccessException;
import com.example.jiraclone.repository.ProjectRepository;
import com.example.jiraclone.repository.UserRepository;
// import com.example.jiraclone.repository.WorkflowStatusRepository; // <-- REMOVED
import com.example.jiraclone.service.ProjectService;
import com.example.jiraclone.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    // private final WorkflowStatusRepository workflowStatusRepository; // <-- REMOVED

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository,
                              SecurityService securityService
            /*, WorkflowStatusRepository workflowStatusRepository */) { // <-- REMOVED from constructor
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
        // this.workflowStatusRepository = workflowStatusRepository; // <-- REMOVED
    }

    @Override
    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO) {
        User currentUser = securityService.getAuthenticatedUser();

        if (projectRepository.findByProjectKey(projectRequestDTO.getProjectKey()).isPresent()) {
            throw new RuntimeException("Project key already exists");
        }

        Project project = new Project();
        project.setName(projectRequestDTO.getName());
        project.setProjectKey(projectRequestDTO.getProjectKey().toUpperCase());
        project.setMembers(Collections.singleton(currentUser));
        project.setOwner(currentUser);

        Project savedProject = projectRepository.save(project);

        // --- REMOVED THE WORKFLOW CREATION LOGIC ---

        return mapToProjectResponseDTO(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsForCurrentUser() {
        User currentUser = securityService.getAuthenticatedUser();
        List<Project> projects = projectRepository.findByMembers_Id(currentUser.getId());
        return projects.stream()
                .map(this::mapToProjectResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long projectId) {
        securityService.checkUserIsMemberOfProject(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        return mapToProjectResponseDTO(project);
    }

    @Override
    @Transactional
    public ProjectResponseDTO addMemberToProject(Long projectId, AddMemberRequestDTO addMemberRequest) {
        securityService.checkUserIsProjectOwner(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        User userToAdd = userRepository.findById(addMemberRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", addMemberRequest.getUserId()));
        project.getMembers().add(userToAdd);
        Project updatedProject = projectRepository.save(project);
        return mapToProjectResponseDTO(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        securityService.checkUserIsProjectOwner(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO projectRequestDTO) {
        securityService.checkUserIsProjectOwner(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        projectRepository.findByProjectKey(projectRequestDTO.getProjectKey()).ifPresent(existingProject -> {
            if (!existingProject.getId().equals(projectId)) {
                throw new RuntimeException("Project key already exists");
            }
        });
        project.setName(projectRequestDTO.getName());
        project.setProjectKey(projectRequestDTO.getProjectKey().toUpperCase());
        Project updatedProject = projectRepository.save(project);
        return mapToProjectResponseDTO(updatedProject);
    }

    @Override
    @Transactional
    public ProjectResponseDTO removeMemberFromProject(Long projectId, Long userId) {
        securityService.checkUserIsProjectOwner(projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (userToRemove.getId().equals(project.getOwner().getId())) {
            throw new UnauthorizedAccessException("The project owner cannot be removed.");
        }
        boolean removed = project.getMembers().remove(userToRemove);
        if (!removed) {
            throw new ResourceNotFoundException("User", "id", userId + " not found in project");
        }
        if (project.getMembers().isEmpty()) {
            project.getMembers().add(userToRemove);
            projectRepository.save(project);
            throw new UnauthorizedAccessException("You cannot remove the last member of a project.");
        }
        Project updatedProject = projectRepository.save(project);
        return mapToProjectResponseDTO(updatedProject);
    }

    private ProjectResponseDTO mapToProjectResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setProjectKey(project.getProjectKey());
        dto.setOwnerId(project.getOwner().getId());

        Set<UserSummaryDTO> members = Collections.emptySet();
        if (project.getMembers() != null) {
            members = project.getMembers().stream().map(user -> {
                UserSummaryDTO userDto = new UserSummaryDTO();
                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());
                return userDto;
            }).collect(Collectors.toSet());
        }
        dto.setMembers(members);
        return dto;
    }
}