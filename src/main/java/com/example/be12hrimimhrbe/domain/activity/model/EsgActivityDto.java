package com.example.be12hrimimhrbe.domain.activity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class EsgActivityDto {
    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ActivityRequest {
        private String subjectId;
        private String subject; // 예: "봉사"
        private LocalDate activityDate;
        private String description;
        private Map<String, Object> inputs; // "봉사시간": 2, "봉사처": "서울숲" 등

        public EsgActivity toEntity(Long memberIdx, Long companyIdx) {
            return EsgActivity.builder()
                    .memberIdx(memberIdx)
                    .companyIdx(companyIdx)
                    .subject(subject)
                    .activityDate(activityDate)
                    .description(description)
                    .inputs(inputs)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ActivityResponse {
        private String activityId;
        private Long memberIdx;
        private Long companyIdx;
        private String subject;
        private String description;
        private Map<String, Object> inputs;
        private LocalDate activityDate;
        private LocalDateTime createdAt;

        public static ActivityResponse fromEntity(EsgActivity entity) {
            return ActivityResponse.builder()
                    .activityId(entity.getId())
                    .memberIdx(entity.getMemberIdx())
                    .companyIdx(entity.getCompanyIdx())
                    .subject(entity.getSubject())
                    .description(entity.getDescription())
                    .inputs(entity.getInputs())
                    .activityDate(entity.getActivityDate())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }
}
