package com.example.jiraclone.service;

import com.example.jiraclone.dto.CreateIssueRequestDTO;
import com.example.jiraclone.dto.IssueResponseDTO;
import com.example.jiraclone.dto.UpdateIssueRequestDTO;
import com.example.jiraclone.dto.UpdateIssueStatusDTO;

import java.util.List;

public interface IssueService {

    IssueResponseDTO createIssue(CreateIssueRequestDTO createIssueRequest);

    List<IssueResponseDTO> getIssuesForProject(Long projectId);

    IssueResponseDTO updateIssueStatus(Long issueId, UpdateIssueStatusDTO statusDTO);

    IssueResponseDTO getIssueById(Long issueId);

    IssueResponseDTO updateIssue(Long issueId, UpdateIssueRequestDTO updateRequest);

    /**
     * Deletes an issue by its ID.
     *
     * @param issueId The ID of the issue to delete.
     */
    void deleteIssue(Long issueId); // <-- ADD THIS
}