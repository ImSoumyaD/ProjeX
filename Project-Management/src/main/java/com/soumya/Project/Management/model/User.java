package com.soumya.Project.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

 /*  the field (e.g., password) can be written when deserializing (taking input),
     but it will not be serialized when producing a JSON response. */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private int projectCount;

    @JsonIgnore
    @ManyToMany(mappedBy = "assignees", cascade = CascadeType.ALL) // one user have many issues
    private List<Issue> assignedIssues = new ArrayList<>();

}
