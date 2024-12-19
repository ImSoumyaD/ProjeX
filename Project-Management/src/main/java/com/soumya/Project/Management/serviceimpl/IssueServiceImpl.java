package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Issue;
import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.IssueRepository;
import com.soumya.Project.Management.request.IssueRequest;
import com.soumya.Project.Management.service.IssueService;
import com.soumya.Project.Management.service.ProjectService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @Override
    public Issue getIssueById(Long issueId) throws Exception {
        Optional<Issue> optionalIssue = issueRepository.findById(issueId);
        if (optionalIssue.isEmpty()) throw new Exception("Issue not found with this id");
        return optionalIssue.get();
    }

    @Override
    public List<Issue> getIssuesByProjectId(Long projectId) throws Exception {
        return issueRepository.findByProjectId(projectId);
    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, User user) throws Exception {
        Project project = projectService.getProjectById(issueRequest.getProjectId());
        if (user != project.getOwner()){
            throw new Exception("only the owner of this project can create new issues");
        }
        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDescription(issueRequest.getDescription());
        issue.setStatus(issueRequest.getStatus());
        issue.setPriority(issueRequest.getPriority());
        issue.setDueDate(issueRequest.getDueDate());
        issue.setTags(issueRequest.getTags());
        issue.setCreatorId(user.getId());
        issue.setProject(project);
        return issueRepository.save(issue);
    }

    @Override
    public Issue updateIssue(Long issueId, IssueRequest updatedIssue, Long userId) throws Exception {
        Issue issue = getIssueById(issueId);
        if (!Objects.equals(userId, issue.getCreatorId())){
            throw new Exception("Only the creator of this issue can update the issue..");
        }
        //update -- title,description,dueDate,tags
        if (updatedIssue.getTitle() != null){
            issue.setTitle(updatedIssue.getTitle());
        }
        if (updatedIssue.getDescription() != null){
            issue.setDescription(updatedIssue.getDescription());
        }
        if (updatedIssue.getDueDate() != null){
            issue.setDueDate(updatedIssue.getDueDate());
        }
        if (updatedIssue.getTags() != null){
            issue.setTags(updatedIssue.getTags());
        }

        return issueRepository.save(issue);
    }

    @Override
    public String deleteIssue(Long issueId, Long userId) throws Exception {
        Issue issue = getIssueById(issueId);
        if (!Objects.equals(issue.getCreatorId(), userId)){
            throw new Exception("you are not authorized to delete this issue..");
        }
        issueRepository.delete(issue);
        return "Issue deleted";
    }

    @Override
    public Issue addUsertoIssue(Long issueId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);
        Project project = issue.getProject();
        if (!project.getTeam().contains(user)){
            throw new Exception("this user is not a team member!");
        }

        if (!issue.getAssignees().contains(user)){
            issue.getAssignees().add(user);
        }
        return issueRepository.save(issue);
    }

    @Override
    public Issue removeUserFromIssue(Long issueId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);
        issue.getAssignees().remove(user);//if user not contained it will do nothing
        return issueRepository.save(issue);
    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {
        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
        return issueRepository.save(issue);
    }

    @Override
    public Issue updatePriority(Long issueId, String priority) throws Exception {
        Issue issue = getIssueById(issueId);
        issue.setPriority(priority);
        return issueRepository.save(issue);
    }
}
