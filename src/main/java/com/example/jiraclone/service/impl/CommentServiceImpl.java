package com.example.jiraclone.service.impl;

import com.example.jiraclone.dto.CommentRequestDTO;
import com.example.jiraclone.dto.CommentResponseDTO;
import com.example.jiraclone.dto.UserSummaryDTO;
import com.example.jiraclone.entity.Comment;
import com.example.jiraclone.entity.Issue;
import com.example.jiraclone.entity.User;
import com.example.jiraclone.exception.CommentNotFoundException;
import com.example.jiraclone.exception.IssueNotFoundException;
import com.example.jiraclone.exception.UnauthorizedAccessException;
import com.example.jiraclone.repository.CommentRepository;
import com.example.jiraclone.repository.IssueRepository;
import com.example.jiraclone.repository.UserRepository;
import com.example.jiraclone.service.CommentService;
import com.example.jiraclone.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public CommentServiceImpl(CommentRepository commentRepository,
                              IssueRepository issueRepository,
                              UserRepository userRepository,
                              SecurityService securityService) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        // ... (createComment logic is unchanged)
        User author = securityService.getAuthenticatedUser();
        if (!author.getId().equals(commentRequestDTO.getAuthorId())) {
            throw new UnauthorizedAccessException("Author ID does not match authenticated user.");
        }
        Issue issue = issueRepository.findById(commentRequestDTO.getIssueId())
                .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + commentRequestDTO.getIssueId()));
        securityService.checkUserIsMemberOfProject(issue.getProject().getId());
        Comment comment = new Comment();
        comment.setBody(commentRequestDTO.getBody());
        comment.setIssue(issue);
        comment.setAuthor(author);
        Comment savedComment = commentRepository.save(comment);
        return mapToCommentResponseDTO(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsForIssue(Long issueId) {
        // ... (getCommentsForIssue logic is unchanged)
        if (!issueRepository.existsById(issueId)) {
            throw new IssueNotFoundException("Issue not found with id: " + issueId);
        }
        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
        return comments.stream()
                .map(this::mapToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        // ... (deleteComment logic is unchanged)
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
        securityService.checkUserIsCommentAuthor(commentId);
        commentRepository.delete(comment);
    }

    // --- ADD THIS NEW METHOD ---
    @Override
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        // 1. Find the comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // 2. --- SECURITY CHECK ---
        // Check if the current user is the author
        securityService.checkUserIsCommentAuthor(commentId);

        // 3. Update the body
        comment.setBody(commentRequestDTO.getBody());

        // 4. Save and return
        Comment updatedComment = commentRepository.save(comment);
        return mapToCommentResponseDTO(updatedComment);
    }

    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setIssueId(comment.getIssue().getId());

        // Create and set the author summary
        UserSummaryDTO authorDto = new UserSummaryDTO();
        authorDto.setId(comment.getAuthor().getId());
        authorDto.setUsername(comment.getAuthor().getUsername());

        dto.setAuthor(authorDto);

        return dto;
    }
}