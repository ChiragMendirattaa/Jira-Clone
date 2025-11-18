package com.example.jiraclone.repository;

import com.example.jiraclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import this
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For login
    Optional<User> findByEmail(String email);

    // For registration check
    Boolean existsByUsername(String username);

    // For registration check
    Boolean existsByEmail(String email);

    /**
     * Searches for users whose username or email contains the search query.
     * Used for adding members to a project.
     *
     * @param username The search query for username.
     * @param email The search query for email.
     * @return A list of matching users.
     */
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
}