package com.example.be12hrimimhrbe.domain.feedback.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedbackTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "company_idx")
    private Company company;

    @OneToMany(mappedBy = "template")
    private List<FeedbackQuestion> questions;
}