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
            return new EsgActivity().builder()
                    .memberIdx(memberIdx)
                    .companyIdx(companyIdx)
                    .subject(subject)
                    .activityDate(activityDate)
                    .description(description)
                    .inputs(inputs)
                    .evidenceImgUrl(imgUrl)
                    .inputs(inputs)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
}
