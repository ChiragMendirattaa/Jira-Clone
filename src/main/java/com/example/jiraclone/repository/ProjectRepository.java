package com.example.jiraclone.repository;

import com.example.jiraclone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import this
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectKey(String projectKey);

    /**
     * Finds all projects where the user with the given ID is a member.
     * Spring Data JPA builds this query for us from the method name.
     */
    List<Project> findByMembers_Id(Long userId);
}