package com.example.be12hrimimhrbe.domain.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FeedbackDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackTemplateRequest{
        private Long companyIdx;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackTemplateResponse{
        private Long templateIdx;
        private List<FeedbackQuestionItem> questions;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackQuestionItem {
        private Long questionIdx;
        private String question;
        private Integer type;
        private Integer sortOrder;
        private List<FeedbackChoiceItem> choices;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackChoiceItem {
        private Long choiceIdx;
        private String text;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackAnswerRequest{
        private List<FeedbackAnswerItem> answers;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackAnswerItem {
        private Long templateIdx;
        private Long questionIdx;
        private Long choiceIdx;
        private String answer;
        private Integer score;
    }
}
