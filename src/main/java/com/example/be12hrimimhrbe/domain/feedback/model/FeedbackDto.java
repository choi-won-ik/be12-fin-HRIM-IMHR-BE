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
        public static FeedbackTemplateResponse from(Long templateIdx, List<FeedbackQuestionItem> questions){
            return FeedbackTemplateResponse.builder().templateIdx(templateIdx).questions(questions).build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackModifyRequest{
        private Long templateIdx;
        private List<FeedbackQuestionItem> deleteQuestions;
        private List<FeedbackQuestionItem> insertQuestions;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackQuestionItem {
        private Long questionIdx;
        private String question;
        private FeedbackQuestion.QuestionType type;
        private Integer sortOrder;
        private List<FeedbackChoiceItem> choices;

        public FeedbackQuestion toEntity(FeedbackTemplate template) {
            return FeedbackQuestion.builder()
                    .template(template)
                    .questionText(question)
                    .type(type)
                    .sortOrder(sortOrder)
                    .isRequired(true)
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FeedbackChoiceItem {
        private Long choiceIdx;
        private String text;

        public FeedbackChoice toEntity(FeedbackQuestion question) {
            return FeedbackChoice.builder()
                    .question(question)
                    .choiceText(text)
                    .value(0) // 기본값 설정
                    .build();
        }
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
