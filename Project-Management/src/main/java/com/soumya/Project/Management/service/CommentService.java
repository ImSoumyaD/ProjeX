package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Comment;
import com.soumya.Project.Management.model.User;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface CommentService {
    Comment createComment(Long issueId, User user,Comment comment) throws Exception;
    String deleteComment(Long commentId, User user) throws Exception;
    List<Comment> getCommentsByIssueId(Long issueId);
}
