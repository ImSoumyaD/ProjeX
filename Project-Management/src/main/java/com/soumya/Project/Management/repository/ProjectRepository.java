package com.soumya.Project.Management.repository;

import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
//    List<Project> findByOwner(User user);

    List<Project> findByNameContainingAndTeamContains(String partialName, User user);
//    List<Project> findByIdAndTeamContains(Long projectId, User user);

//    @Query("select p from Project p join p.team t where t=:user")
//    List<Project> findProjectByTeam(@Param("user") User user);

    //list of projects of which the user is owner or user is part of team
    List<Project> findByTeamContainingOrOwner(User user,User owner);
}
