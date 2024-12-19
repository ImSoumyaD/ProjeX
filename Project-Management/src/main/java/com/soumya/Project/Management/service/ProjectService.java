package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.Chat;
import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project,User user) throws Exception;
    List<Project> getProjectsByTeam(User user, String category, String tag);
    Project getProjectById(Long projectId) throws Exception;
    String deleteProjectById(Long projectId, User user) throws Exception;
    Project updateProject(Project updatedProject, Long projectId,User user) throws Exception;
    String addUserToProject(Long projectId, Long userId) throws Exception;
    String removeUserFromProject(User owner,Long projectId, Long userId) throws Exception;
    Chat getChatByProjectId(Long projectId) throws Exception;
    List<Project> searchProject(String keyword, User user) throws Exception;
}
