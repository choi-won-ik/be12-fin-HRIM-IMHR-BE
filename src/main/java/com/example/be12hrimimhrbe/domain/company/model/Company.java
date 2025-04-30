package com.example.be12hrimimhrbe.domain.company.model;

import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String name;

    private boolean isMember;

    private boolean has_esg_data;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String imgUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 10)
    private String code;

    @OneToMany(mappedBy = "company")
    private List<Member> members=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @Fetch(FetchMode.SUBSELECT)
    private List<Department> departments=new ArrayList<>();
}
