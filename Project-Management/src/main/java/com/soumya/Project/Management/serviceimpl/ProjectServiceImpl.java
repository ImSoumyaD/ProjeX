package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.model.Chat;
import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.ProjectRepository;
import com.soumya.Project.Management.service.ChatService;
import com.soumya.Project.Management.service.ProjectService;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;

    @Override
    public Project createProject(Project project, User user) throws Exception {
        Project newProject = new Project();
        newProject.setOwner(user);
        newProject.setTags(project.getTags());
        newProject.setName(project.getName());
        newProject.setCategory(project.getCategory());
        newProject.setDescription(project.getDescription());
        newProject.setDueDate(project.getDueDate());
        newProject.setCreatedOn(LocalDate.now());
        newProject.setStatus(project.getStatus());
        newProject.getTeam().add(user);


//        userService.updateUserProjectCount(user);//update the project count of the user
        Project savedProject = projectRepository.save(newProject);

        Chat chat = new Chat();

        chat.setProject(savedProject);// Associate the chat with the project
        chat.getUsers().add(user); // add the creator to the chat
        Chat projectChat = chatService.createChat(chat);//save the chat
        savedProject.setChat(projectChat); // add that chat in project

        return savedProject;
    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) {
        List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);

        //filter by category
        if (category != null){
            projects = projects.stream().filter(project -> project.getCategory().equals(category))
                    .toList();
        }
        //filer by tag
        if (tag != null){
            projects = projects.stream().filter(project -> project.getTags().contains(tag))
                    .toList();
        }


        return projects;
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {
        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) throw new Exception("project not found with this id");
        return optProject.get();
    }

    @Override
    public String deleteProjectById(Long projectId, User user) throws Exception {
        Project project = getProjectById(projectId);
        if (!Objects.equals(user, project.getOwner())){
            throw new Exception("Only the owner of the project can delete the project");
        }
        projectRepository.delete(project);
        return "Project deleted successfully..";
    }

    @Override
    public Project updateProject(Project updatedProject, Long projectId,User user) throws Exception {
        Project project = getProjectById(projectId);
        if (project.getOwner() != user){
            throw new Exception("only owner of this project can edit the project");
        }
        if (updatedProject.getName() != null){
            project.setName(updatedProject.getName());
        }
        if (updatedProject.getDescription() != null){
            project.setDescription(updatedProject.getDescription());
        }
        if (!updatedProject.getTags().isEmpty()){
            project.setTags(updatedProject.getTags());
        }
        if (updatedProject.getStatus() != null){
            project.setStatus(updatedProject.getStatus());
        }
        if (updatedProject.getDueDate() != null){
            project.setDueDate(updatedProject.getDueDate());
        }

        return projectRepository.save(project);
    }

    @Override
    public String addUserToProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (!project.getTeam().contains(user)){
            project.getTeam().add(user);
            project.getChat().getUsers().add(user);
            projectRepository.save(project);
            return "user added to the project successfully..";
        }
        return "user already associated with this project";
    }

    @Override
    public String removeUserFromProject(User owner, Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (project.getOwner() != owner){
            throw new Exception("only owner of this project can remove user from team");
        }
        if (project.getTeam().contains(user)){
            project.getTeam().remove(user);
            project.getChat().getUsers().remove(user);
            projectRepository.save(project);
            return "user removed from the project";
        }
        return "user is not associated with this project";
    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {
        Project project = getProjectById(projectId);
        return project.getChat();
    }

    //search by name if the project and where the user is part of team
    @Override
    public List<Project> searchProject(String keyword, User user) throws Exception {
        return projectRepository.findByNameContainingAndTeamContains(keyword,user);
    }
}
