package com.example.jiraclone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- Relationships ---

    // A user can be part of many projects
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects;

    // A user can be assigned many issues
    @OneToMany(mappedBy = "assignee")
    private Set<Issue> assignedIssues;

    // A user can report many issues
    @OneToMany(mappedBy = "reporter")
    private Set<Issue> reportedIssues;

    // A user can write many comments
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

    // --- UserDetails Methods (for Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We are not using roles (like ROLE_ADMIN) yet,
        // so we return an empty list.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // Returns the user's hashed password
        return this.password;
    }

    @Override
    public String getUsername() {
        // We are using email as the unique identifier for login
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // For simplicity, we'll just return true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // For simplicity, we'll just return true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // For simplicity, we'll just return true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // For simplicity, we'll just return true
        return true;
    }
}