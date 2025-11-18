package com.example.jiraclone.service;

import com.example.jiraclone.dto.AddMemberRequestDTO;
import com.example.jiraclone.dto.ProjectRequestDTO;
import com.example.jiraclone.dto.ProjectResponseDTO;
import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO);

    List<ProjectResponseDTO> getProjectsForCurrentUser();

    ProjectResponseDTO getProjectById(Long projectId);

    ProjectResponseDTO addMemberToProject(Long projectId, AddMemberRequestDTO addMemberRequest);

    void deleteProject(Long projectId);

    /**
     * Updates a project's details.
     * @param projectId The ID of the project to update.
     * @param projectRequestDTO DTO with new name and key.
     * @return The updated project DTO.
     */
    ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO projectRequestDTO); // <-- ADD

    /**
     * Removes a member from a project.
     * @param projectId The ID of the project.
     * @param userId The ID of the user to remove.
     * @return The updated project DTO.
     */
    ProjectResponseDTO removeMemberFromProject(Long projectId, Long userId); // <-- ADD
}