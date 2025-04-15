package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackResponse;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
    List<FeedbackResponse> findAllByFrom(Member from);
    List<FeedbackResponse> findAllByTo(Member to);
}
