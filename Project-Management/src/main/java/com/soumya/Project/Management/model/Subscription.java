package com.soumya.Project.Management.model;

import com.soumya.Project.Management.enums.PlanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;
    private PlanType planType;
    private boolean isValid; // to check if subscription is expired

    @OneToOne
    private User user;

}
