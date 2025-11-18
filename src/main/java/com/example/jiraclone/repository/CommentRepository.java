package com.example.jiraclone.repository;

import com.example.jiraclone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // To load all comments for an issue
    List<Comment> findByIssueIdOrderByCreatedAtAsc(Long issueId);
}