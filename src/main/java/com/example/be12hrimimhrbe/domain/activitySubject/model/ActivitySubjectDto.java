package com.example.be12hrimimhrbe.domain.activitySubject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ActivitySubjectDto {

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ActivitySubjectRequest {
        private String id;
        private Long companyIdx;
        private String subject;
        private String esgValue;
        private int esgScore;
        private String esgActivityItem;
        private String evaluationCriteria;
        private List<InputRequest> inputs;

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class InputRequest {
            private String text;
            private String type;
        }
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ActivitySubjectResponse {
        private String id;
        private String subject;
        private String esgValue;
        private int esgScore;
        private String esgActivityItem;
        private String evaluationCriteria;
        private List<InputResponse> inputs;

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class InputResponse {
            private String text;
            private String type;
        }

        public static ActivitySubjectResponse from(ActivitySubject activitySubject) {
            return builder()
                    .id(activitySubject.getId())
                    .subject(activitySubject.getSubject())
                    .esgValue(activitySubject.getEsgValue())
                    .esgScore(activitySubject.getEsgScore())
                    .esgActivityItem(activitySubject.getEsgActivityItem())
                    .evaluationCriteria(activitySubject.getEvaluationCriteria())
                    .inputs(activitySubject.getInputs().stream()
                            .map(i -> InputResponse.builder()
                                    .type(i.getType())
                                    .text(i.getText())
                                    .build()
                            ).toList()
                    ).build();
        }
    }
}
