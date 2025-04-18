package com.example.be12hrimimhrbe.domain.member.model;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @Enumerated(EnumType.STRING)
//    private Role role = Role.EMPLOYEE;

    @Column(nullable = false)
    private Boolean isAdmin;
    @Column(nullable = false)
    private Boolean hasProdAuth;
    @Column(nullable = false)
    private Boolean hasPartnerAuth;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "company_idx")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "department_idx")
    private Department department;

    private LocalDateTime joinedAt = LocalDateTime.now();

//    public enum Role { EMPLOYEE, MANAGER, EXECUTIVE, MASTER }
    public enum Status { PENDING, APPROVED }

    private int eScore;
    private int sScore;
    private int gScore;

    @Column(length = 20)
    private String code;

    private int notificationCount;

//    @OneToMany(mappedBy = "member")
//    @BatchSize(size = 5)
//    private List<Activity> activities=new ArrayList<>();

    public void approve() {
        this.status = Status.APPROVED;
    }

public Member updateMember(Member member) {
        return Member.builder()
                .idx(this.getIdx())
                .memberId(this.memberId)
                .name(member.getName() == null ? this.name : member.getName())
                .email(this.email)
                .password(member.getPassword() == null ? this.password : member.getPassword())
                .joinedAt(this.joinedAt)
                .isAdmin(member.getIsAdmin() == null? this.isAdmin : member.getIsAdmin())
                .hasProdAuth(member.hasProdAuth == null? this.hasProdAuth : member.hasProdAuth)
                .hasPartnerAuth(member.getHasPartnerAuth() == null? this.hasPartnerAuth : member.hasPartnerAuth)
                .status(this.status)
                .company(this.company)
                .department(member.getDepartment() == null ? this.department : member.getDepartment())
                .eScore(this.eScore)
                .sScore(this.sScore)
                .gScore(this.gScore)
                .code(this.code)
                .build();
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }
}
