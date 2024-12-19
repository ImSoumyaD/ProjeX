package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Issue;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.request.IssueRequest;

import java.util.List;

public interface IssueService {
    Issue getIssueById(Long issueId) throws Exception;
    List<Issue> getIssuesByProjectId(Long projectId) throws Exception;
    Issue createIssue(IssueRequest issueRequest,User user) throws Exception;
    Issue updateIssue(Long issueId,IssueRequest updatedIssue,Long userId) throws Exception;
    String deleteIssue(Long issueId,Long userId) throws Exception;
//    List<Issue> getIssueByAssigneeId(Long assigneeId);
//    List<Issue> searchIssue(String title,String status,String priority,Long assigneeId) throws Exception;
//    List<User> getAssigneeForIssue(Long issueId);
    Issue addUsertoIssue(Long issueId,Long userId) throws Exception;
    Issue removeUserFromIssue(Long issueId,Long userId) throws Exception;
    Issue updateStatus(Long issueId,String status) throws Exception;
    Issue updatePriority(Long issueId,String priority) throws Exception;

}
