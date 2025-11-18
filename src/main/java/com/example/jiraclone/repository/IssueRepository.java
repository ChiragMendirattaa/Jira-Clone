package com.example.jiraclone.repository;

import com.example.jiraclone.entity.Issue;
import com.example.jiraclone.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    // To load the Kanban board for a project
    List<Issue> findByProjectId(Long projectId);

    // To find all issues assigned to a specific user
    List<Issue> findByAssigneeId(Long assigneeId);

    // To get all issues for a project, by status (useful for the board)
    List<Issue> findByProjectIdAndStatus(Long projectId, IssueStatus status);
}