package com.example.be12hrimimhrbe.domain.event.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class EventDto {
    @Getter @AllArgsConstructor @NoArgsConstructor
    @Schema(description = "일정 등록 / 수정 요청 DTO")
    public static class EventRequest {
        @Schema(description = "일정 고유 식별 값", example = "1")
        private Long idx;
        @Schema(description = "일정 제목", example = "부서별 워크숍")
        private String title;
        @Schema(description = "일정 내용", example = "회계부서의 위크숍일정입니다.")
        private String content;
        @Schema(description = "일정 색깔", example = "#94D4F3")
        private String color;
        @Schema(description = "시작 날짜", example = "2025-01-01")
        private LocalDate startDate;
        @Schema(description = "끝 날짜", example = "2025-01-02")
        private LocalDate endDate;

        public Event toEntity(Company company) {
            return Event.builder()
                    .idx(idx)
                    .title(title)
                    .content(content)
                    .color(color)
                    .startDate(startDate)
                    .endDate(endDate)
                    .company(company)
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class EventResponse {
        @Schema(description = "일정 고유 식별 값", example = "1")
        private Long idx;
        @Schema(description = "일정 제목", example = "부서별 워크숍")
        private String title;
        @Schema(description = "일정 내용", example = "회계부서의 위크숍일정입니다.")
        private String content;
        @Schema(description = "일정 색깔", example = "#94D4F3")
        private String color;
        @Schema(description = "시작 날짜", example = "2025-01-01")
        private LocalDate startDate;
        @Schema(description = "끝 날짜", example = "2025-01-02")
        private LocalDate endDate;

        public static EventResponse of(Event event) {
            return builder()
                    .idx(event.getIdx())
                    .title(event.getTitle())
                    .content(event.getContent())
                    .color(event.getColor())
                    .startDate(event.getStartDate())
                    .endDate(event.getEndDate())
                    .build();
        }
    }
}
