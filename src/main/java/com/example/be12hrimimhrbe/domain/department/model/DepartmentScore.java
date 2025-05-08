package com.example.be12hrimimhrbe.domain.department.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "department_score")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "department_idx", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "company_idx", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false)
    private Integer environment;

    @Column(nullable = false)
    private Integer social;

    @Column(nullable = false)
    private Integer governance;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}