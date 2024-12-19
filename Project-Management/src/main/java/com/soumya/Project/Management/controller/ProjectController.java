package com.soumya.Project.Management.controller;

import com.soumya.Project.Management.model.Chat;
import com.soumya.Project.Management.model.Invitation;
import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.request.InviteRequest;
import com.soumya.Project.Management.service.InvitationService;
import com.soumya.Project.Management.service.ProjectService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
    @Autowired
    InvitationService invitationService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestHeader("Authorization") String jwt,@RequestBody Project project) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Project createdProject = projectService.createProject(project, user);
        return new ResponseEntity<>(createdProject,HttpStatus.CREATED);
    }

    @PatchMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@RequestHeader("Authorization") String jwt,@PathVariable Long projectId,@RequestBody Project project) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Project updatedProject = projectService.updateProject(project,projectId,user);
        return new ResponseEntity<>(updatedProject,HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        List<Project> projects = projectService.getProjectsByTeam(user, category, tag);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@RequestHeader("Authorization") String jwt,@PathVariable Long projectId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Project project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String jwt,@PathVariable Long projectId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        String response = projectService.deleteProjectById(projectId,user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProject(@RequestHeader("Authorization") String jwt,@RequestParam String name) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        List<Project> projects = projectService.searchProject(name, user);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<?> getChatByProjectId(@RequestHeader("Authorization") String jwt,@PathVariable Long projectId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Chat chat = projectService.getChatByProjectId(projectId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteToProject(
            @RequestHeader("Authorization") String jwt,
            @RequestBody InviteRequest inviteRequest) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        invitationService.sendInvitation(inviteRequest.getEmail(),inviteRequest.getProjectId());
        return new ResponseEntity<>("Invitation successfully sent",HttpStatus.ACCEPTED);
    }

    @GetMapping("/accept-invitation")
    public ResponseEntity<?> acceptInvitationToProject(@RequestHeader("Authorization") String jwt,@RequestParam String token) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token, user.getId());
        projectService.addUserToProject(invitation.getProjectId(), user.getId());
//        invitationService.deleteInvitationByToken(token); //--delete the token after invitation is accepted
        return new ResponseEntity<>(invitation,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{projectId}/remove/{userId}")
    public ResponseEntity<?> removeUserFromTeam(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long userId,
            @PathVariable Long projectId) throws Exception {
        User reqUser = userService.findProfileByJwt(jwt);
        String response = projectService.removeUserFromProject(reqUser, projectId, userId);
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }
}
