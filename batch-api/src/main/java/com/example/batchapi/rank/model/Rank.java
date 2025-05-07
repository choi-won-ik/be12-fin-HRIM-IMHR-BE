package com.example.batchapi.rank.model;

import com.example.batchapi.entity.Company;
import com.example.batchapi.entity.Member;
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

    @ManyToOne
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
}
