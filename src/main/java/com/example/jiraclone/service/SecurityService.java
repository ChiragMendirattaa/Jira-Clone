package com.example.jiraclone.service; // (Your package name)

import com.example.jiraclone.entity.User;

public interface SecurityService {

    User getAuthenticatedUser();

    void checkUserIsMemberOfProject(Long projectId);

    void checkUserIsCommentAuthor(Long commentId);

    void checkUserIsProjectOwner(Long projectId); // <-- ADD THIS
}