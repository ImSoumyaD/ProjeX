package com.soumya.Project.Management.DTO;

import com.soumya.Project.Management.model.Project;
import com.soumya.Project.Management.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long creatorId;
    private LocalDate dueDate;
    private List<String> tags = new ArrayList<>();
    private List<User> assignees;
    private Project project;
}
