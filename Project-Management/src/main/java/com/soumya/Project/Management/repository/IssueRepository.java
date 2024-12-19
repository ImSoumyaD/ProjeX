package com.soumya.Project.Management.repository;

import com.soumya.Project.Management.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long> {
    @Query("select i from Issue i where i.project.id=:projectId")
    List<Issue> findByProjectId(Long projectId);
}
