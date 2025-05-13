package com.example.be12hrimimhrbe.domain.rank.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rank)) return false;
        Rank rank = (Rank) o;
        return idx != null && idx.equals(rank.idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idx);
    }
}
