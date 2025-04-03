package com.example.be12hrimimhrbe.domain.score.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class ScoreDto {
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DashBoardReq {
        @Schema(description = "회사 Idx", example = "1")
        private Long companyIdx;
    }

    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DashBoardRsp {
        @Schema(description = "회사명", example = "한화시스템")
        private String companyName;
        @Schema(description = "esg 환경 점수", example = "55")
        private int environmentScore;
        @Schema(description = "esg 사회 점수", example = "73")
        private Integer socialScore;
        @Schema(description = "esg 지배구조 점수", example = "80")
        private Integer governanceScore;
        @Schema(description = "esg 총 점수", example = "74")
        private Integer totalScore;
        @Schema(description = "연도별 esg 점수 변화",
        example = "[{\"year\":2021,\"score\":70},{\"year\":2022,\"score\":71}]")
        private List<ChageScoreRsp> chageScoreRsp;
    }

    public static class ChageScoreRsp {

        @Schema(description = "년도", example = "2021")
        private Integer year;

        @Schema(description = "해당 연도 ESG 점수", example = "70")
        private Integer score;
    }
}
