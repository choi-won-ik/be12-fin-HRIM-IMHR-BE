package com.example.be12hrimimhrbe.domain.feedback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedbackQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "template_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FeedbackTemplate template;

    @OneToMany(mappedBy = "question")
    private List<FeedbackChoice> choices;

    @Lob
    private String questionText;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private Boolean isRequired = true;

    private Integer value;

    private Integer sortOrder;

    public enum QuestionType { SUBJECTIVE, MULTIPLE_CHOICE }
}