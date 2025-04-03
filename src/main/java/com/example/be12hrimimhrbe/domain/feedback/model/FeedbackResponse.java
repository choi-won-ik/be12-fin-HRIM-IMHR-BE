package com.example.be12hrimimhrbe.domain.feedback.model;

import com.example.be12hrimimhrbe.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedbackResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private FeedbackTemplate feedback;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private FeedbackQuestion question;

    @ManyToOne
    @JoinColumn(name = "choice_id")
    private FeedbackChoice choice;

    @Lob
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member from;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member to;

    private LocalDateTime createdAt = LocalDateTime.now();
}