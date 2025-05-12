package com.example.be12hrimimhrbe.domain.department.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    private int targetEScore;

    @Setter
    private int targetSScore;

    @Setter
    private int targetGScore;

    @Setter
    @Column(nullable = false)
    private Boolean is_deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Company company;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private List<Member> members = new ArrayList<>();

}