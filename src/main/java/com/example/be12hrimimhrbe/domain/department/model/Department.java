package com.example.be12hrimimhrbe.domain.department.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_idx", nullable = false)
    private Company company;
}