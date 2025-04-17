package com.example.be12hrimimhrbe.domain.feedback.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "feedback_idx")
    private FeedbackTemplate feedback;

    @ManyToOne
    @JoinColumn(name = "question_idx")
    private FeedbackQuestion question;

    @ManyToOne
    @JoinColumn(name = "choice_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FeedbackChoice choice;

    @Lob
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "from_member_idx")
    private Member from;

    @ManyToOne
    @JoinColumn(name = "to_member_idx")
    private Member to;

    private LocalDateTime createdAt = LocalDateTime.now();
}