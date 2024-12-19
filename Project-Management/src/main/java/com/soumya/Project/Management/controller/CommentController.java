package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.model.Comment;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.service.CommentService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @PostMapping("/{issueId}")
    public ResponseEntity<?> createComment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long issueId,
            @RequestBody Comment comment) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        Comment createdComment = commentService.createComment(issueId, user, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long commentId) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        String response = commentService.deleteComment(commentId, user);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/issue/{issueId}")
    public List<Comment> getCommentsByIssueId(@PathVariable Long issueId){
        return commentService.getCommentsByIssueId(issueId);
    }


}
