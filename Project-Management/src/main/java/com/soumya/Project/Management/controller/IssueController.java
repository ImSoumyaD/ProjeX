package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.DTO.IssueDto;
import com.soumya.Project.Management.model.Issue;
import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.request.IssueRequest;
import com.soumya.Project.Management.service.IssueService;
import com.soumya.Project.Management.service.UserService;
import org.aspectj.apache.bcel.classfile.LineNumber;
import org.aspectj.apache.bcel.generic.LineNumberGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.RecursiveTask;

@RestController
@RequestMapping("/api/issue")
public class IssueController {
    @Autowired
    IssueService issueService;
    @Autowired
    UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<?> getIssueById(@PathVariable Long issueId) throws Exception {
        Issue issue = issueService.getIssueById(issueId);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getIssueByProjectId(@PathVariable Long projectId) throws Exception {
        List<Issue> issues = issueService.getIssuesByProjectId(projectId);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createIssue(@RequestBody IssueRequest issueRequest, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Issue issue = issueService.createIssue(issueRequest, user);
        IssueDto issueDto = new IssueDto();
        issueDto.setId(issue.getId());
        issueDto.setCreatorId(issue.getCreatorId());
        issueDto.setTitle(issue.getTitle());
        issueDto.setDescription(issue.getDescription());
        issueDto.setPriority(issue.getPriority());
        issueDto.setStatus(issue.getStatus());
        issueDto.setProject(issue.getProject());
        issueDto.setTags(issue.getTags());
        issueDto.setAssignees(issue.getAssignees());
        issueDto.setDueDate(issue.getDueDate());
        return ResponseEntity.ok(issueDto);
    }

    @DeleteMapping("/delete/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        String response = issueService.deleteIssue(issueId, user.getId());
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{issueId}/assign-user/{userId}")
    public ResponseEntity<?> addUserToIssue(@RequestHeader("Authorization") String jwt,@PathVariable Long issueId, @PathVariable Long userId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Project project = issueService.getIssueById(issueId).getProject();
        if (project.getOwner() != user){
            throw new Exception("only owner of this project can assign issues..");
        }

        Issue newIssue = issueService.addUsertoIssue(issueId, userId);
        return new ResponseEntity<>(newIssue,HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{issueId}/remove/{userId}")
    public ResponseEntity<?> removeUserFromIssue (@RequestHeader("Authorization") String jwt,@PathVariable Long issueId, @PathVariable Long userId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Project project = issueService.getIssueById(issueId).getProject();
        if (project.getOwner() != user){
            throw new Exception("only owner can remove users from issues..");
        }
        Issue issue = issueService.removeUserFromIssue(issueId, userId);
        return new ResponseEntity<>(issue,HttpStatus.ACCEPTED);
    }

    //update only - title,description,dueDate,tags
    @PutMapping("/{issueId}/update")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long issueId,
            @RequestBody IssueRequest issueRequest,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Issue updatedIssue = issueService.updateIssue(issueId, issueRequest,user.getId());
        return new ResponseEntity<>(updatedIssue,HttpStatus.OK);
    }

        @PutMapping("/{issueId}/update-status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable Long issueId, @PathVariable String status) throws Exception {
        Issue issue = issueService.updateStatus(issueId, status);
        return new ResponseEntity<>(issue,HttpStatus.OK);
    }

    @PutMapping("/{issueId}/update-priority/{priority}")
    public ResponseEntity<?> updatePriority(@PathVariable Long issueId, @PathVariable String priority) throws Exception {
        Issue issue = issueService.updatePriority(issueId, priority);
        return new ResponseEntity<>(issue,HttpStatus.OK);
    }

}
