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
        private int esgScore;
        private String esgValue;
        private String esgActivityItem;
        private LocalDate activityDate;
        private Map<String, Object> inputs; // "봉사시간": 2, "봉사처": "서울숲" 등
        private Boolean status;

        public EsgActivity toEntity(String userID, String name, Long memberIdx, Long companyIdx) {
            return EsgActivity.builder()
                    .memberIdx(memberIdx)
                    .userName(name)
                    .companyIdx(companyIdx)
                    .userID(userID)
                    .subject(subject)
                    .esgScore(esgScore)
                    .esgValue(esgValue)
                    .status(status)
                    .activityDate(activityDate)
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
        private String esgActivityItem;
        private String subject;
        private Map<String, Object> inputs;
        private LocalDate activityDate;
        private String userName;
        private String userID;
        private LocalDateTime createdAt;
        private Boolean status;

        public static ActivityResponse fromEntity(EsgActivity entity) {
            return ActivityResponse.builder()
                    .activityId(entity.getId())
                    .memberIdx(entity.getMemberIdx())
                    .userName(entity.getUserName())
                    .esgActivityItem(entity.getEsgActivityItem())
                    .companyIdx(entity.getCompanyIdx())
                    .subject(entity.getSubject())
                    .userID(entity.getUserID())
                    .status(entity.getStatus())
                    .inputs(entity.getInputs())
                    .activityDate(entity.getActivityDate())
                    .createdAt(entity.getCreatedAt())
                    .build();
        }
    }
}
