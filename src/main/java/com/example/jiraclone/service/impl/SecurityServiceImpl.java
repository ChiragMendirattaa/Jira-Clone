package com.example.jiraclone.service.impl; // (Or your package name)

import com.example.jiraclone.entity.Comment;
import com.example.jiraclone.entity.Project;
import com.example.jiraclone.entity.User;
import com.example.jiraclone.exception.CommentNotFoundException;
import com.example.jiraclone.exception.ProjectNotFoundException;
import com.example.jiraclone.exception.UnauthorizedAccessException;
import com.example.jiraclone.repository.CommentRepository;
import com.example.jiraclone.repository.ProjectRepository;
import com.example.jiraclone.repository.UserRepository;
import com.example.jiraclone.service.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository; // <-- ADDED

    public SecurityServiceImpl(UserRepository userRepository,
                               ProjectRepository projectRepository,
                               CommentRepository commentRepository) { // <-- ADDED
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository; // <-- ADDED
    }

    @Override
    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found with email: " + userEmail));
    }

    @Override
    @Transactional(readOnly = true)
    public void checkUserIsMemberOfProject(Long projectId) {
        User currentUser = getAuthenticatedUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        // Check if the user is in the project's member set
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));

        if (!isMember) {
            throw new UnauthorizedAccessException("User is not a member of this project");
        }
    }

    // --- THIS IS THE NEW METHOD ---
    @Override
    @Transactional(readOnly = true)
    public void checkUserIsCommentAuthor(Long commentId) {
        User currentUser = getAuthenticatedUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Check if the current user's ID matches the comment author's ID
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("User is not the author of this comment");
        }
    }
    @Override
    @Transactional(readOnly = true)
    public void checkUserIsProjectOwner(Long projectId) {
        User currentUser = getAuthenticatedUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        // Check if the current user's ID matches the project owner's ID
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("User is not the owner of this project");
        }
    }
}