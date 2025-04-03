package com.example.be12hrimimhrbe.domain.score.model;

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
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String companyCode;

    private Integer environmentScore;
    private Integer socialScore;
    private Integer governanceScore;
    private Integer totalScore;

    private Integer year;
}