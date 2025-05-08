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
        private List<SubjectRequest> subjects;

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class SubjectRequest {
            private String subject;
            private List<InputRequest> inputs;

            @Getter @Builder @NoArgsConstructor @AllArgsConstructor
            public static class InputRequest {
                private String text;
                private String type;
            }
        }
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ActivitySubjectResponse {
        private String id;
        private List<SubjectResponse> subjects;

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class SubjectResponse {
            private String subject;
            private List<InputResponse> inputs;

            @Getter @Builder @NoArgsConstructor @AllArgsConstructor
            public static class InputResponse {
                private String text;
                private String type;
            }
        }

        public static ActivitySubjectResponse from(ActivitySubject activitySubject) {
            return builder()
                    .id(activitySubject.getId())
                    .subjects(activitySubject.getSubjects().stream()
                            .map(s -> SubjectResponse.builder()
                                    .subject(s.getSubject())
                                    .inputs(
                                            s.getInputs().stream()
                                                    .map(i -> SubjectResponse.InputResponse.builder()
                                                            .text(i.getText())
                                                            .type(i.getType())
                                                            .build()
                                                    ).toList()
                                    ).build()
                            ).toList()
                    ).build();
        }
    }
}
