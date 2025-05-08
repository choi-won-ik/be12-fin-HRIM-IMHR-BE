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
        private List<SubjectDto> subjects;

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class SubjectDto {
            private String subject;
            private List<InputDto> inputs;

            @Getter @Builder @NoArgsConstructor @AllArgsConstructor
            public static class InputDto {
                private String text;
                private String type;
            }
        }
    }
}
