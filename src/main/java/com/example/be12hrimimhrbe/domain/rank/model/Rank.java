package com.example.be12hrimimhrbe.domain.rank.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ranking")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    @Setter
    private Member member;

    @ManyToOne
    @JoinColumn(name = "company_idx")
    private Company company;

    @Setter
    private int average;

    @Column(nullable = false)
    private int ranking;

    @Column(nullable = false)
    private int year;
    @Column(nullable = false)
    private int month;
}
