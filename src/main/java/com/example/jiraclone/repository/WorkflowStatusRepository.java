package com.example.jiraclone.repository;

import com.example.jiraclone.entity.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowStatusRepository extends JpaRepository<WorkflowStatus, Long> {

    // Find all statuses for a project, sorted by their order
    List<WorkflowStatus> findByProjectIdOrderByOrderAsc(Long projectId);

    // Find the first status (e.g., "To Do") for a new issue
    Optional<WorkflowStatus> findFirstByProjectIdOrderByOrderAsc(Long projectId);
}