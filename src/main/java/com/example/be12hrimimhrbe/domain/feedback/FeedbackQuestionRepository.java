package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackQuestion;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackQuestionRepository extends JpaRepository<FeedbackQuestion, Long> {
    List<FeedbackQuestion> findByTemplate(FeedbackTemplate template);
}