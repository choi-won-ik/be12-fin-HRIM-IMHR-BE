package com.example.be12hrimimhrbe.domain.score.model;

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
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, length = 3)
    private String environmentScore;
    @Column(nullable = false, length = 3)
    private String socialScore;
    @Column(nullable = false, length = 3)
    private String governanceScore;
    @Column(nullable = false, length = 3)
    private String totalScore;

    @Column(nullable = false, length = 4)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "company_idx")
    private Company company;

}