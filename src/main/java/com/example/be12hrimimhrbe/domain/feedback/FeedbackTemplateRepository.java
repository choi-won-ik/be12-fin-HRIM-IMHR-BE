package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackTemplate;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackTemplateRepository extends JpaRepository<FeedbackTemplate, Long> {
    List<FeedbackTemplate> findAllByCompanyOrderByCreatedAtDesc(Company company, Limit limit);
} 