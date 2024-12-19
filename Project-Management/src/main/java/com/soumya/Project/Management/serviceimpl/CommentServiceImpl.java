package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Comment;
import com.soumya.Project.Management.model.Issue;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.CommentRepository;
import com.soumya.Project.Management.service.CommentService;
import com.soumya.Project.Management.service.IssueService;
import com.soumya.Project.Management.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    IssueService issueService;

    @Override
    public Comment createComment(Long issueId, User user, Comment comment) throws Exception {
        Issue issue = issueService.getIssueById(issueId);
        comment.setUser(user);
        comment.setIssue(issue);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        issue.getComments().add(savedComment);
        return savedComment;
    }

    @Override
    public String deleteComment(Long commentId, User user) throws Exception {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()){
            throw new BadRequestException("no comment found with this id");
        }
        Comment comment = optionalComment.get();
        if (!Objects.equals(user,comment.getUser())){
            throw new Exception("you can not delete other user's comment..");
        }
        commentRepository.delete(comment);
        return "comment deleted..";
    }

    @Override
    public List<Comment> getCommentsByIssueId(Long issueId) {
        return commentRepository.findByIssueId(issueId);
    }
}
