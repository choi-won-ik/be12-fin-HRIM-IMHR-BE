package com.example.be12hrimimhrbe.domain.company.model;

import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.rank.model.Rank;
import jakarta.persistence.*;
import lombok.*;
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

    @Setter
    private int targetEScore;
    @Setter
    private int targetSScore;
    @Setter
    private int targetGScore;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String imgUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 10)
    private String code;

    @OneToMany(mappedBy = "company")
    private List<Member> members=new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Department> departments=new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Rank> ranks=new ArrayList<>();
}
