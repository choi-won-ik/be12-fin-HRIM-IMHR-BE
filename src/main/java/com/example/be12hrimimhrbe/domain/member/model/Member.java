package com.example.be12hrimimhrbe.domain.member.model;

import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.EMPLOYEE;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private LocalDateTime joinedAt = LocalDateTime.now();

    public enum Role { EMPLOYEE, MANAGER, EXECUTIVE, MASTER }
    public enum Status { PENDING, APPROVED }
}
