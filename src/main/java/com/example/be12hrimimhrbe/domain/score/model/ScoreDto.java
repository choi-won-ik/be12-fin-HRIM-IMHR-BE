package com.example.be12hrimimhrbe.domain.score.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class ScoreDto {


    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DashBoardRsp {
        @Schema(description = "회사명", example = "한화시스템")
        private String companyName;

        @Schema(description = "연도별 esg 점수 변화",
        example = "[{\"year\":2021,\"score\":70},{\"year\":2022,\"score\":71}]")
        private List<ChangeScoreRsp> changeScoreRsp;
    }
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ChangeScoreRsp {

        @Schema(description = "년도", example = "2021")
        private Integer year;
        @Schema(description = "esg 환경 점수", example = "A")
        private String environmentScore;
        @Schema(description = "esg 사회 점수", example = "A")
        private String socialScore;
        @Schema(description = "esg 지배구조 점수", example = "B+")
        private String governanceScore;
        @Schema(description = "해당 연도 ESG 점수", example = "70")
        private Integer score;
        public ChangeScoreRsp(Score score) {
            this.year = score.getYear();
            this.environmentScore = score.getEnvironmentScore();
            this.socialScore = score.getSocialScore();
            this.governanceScore = score.getGovernanceScore();

            if(score.getTotalScore().equals("S")) {
                this.score = 85;
            } else if (score.getTotalScore().equals("A+")) {
                this.score = 80;
            }else if(score.getTotalScore().equals("A")) {
                this.score = 75;
            }else if(score.getTotalScore().equals("B+")) {
                this.score = 70;
            }else if(score.getTotalScore().equals("B")) {
                this.score = 65;
            }else if(score.getTotalScore().equals("C+")) {
                this.score = 60;
            }else if(score.getTotalScore().equals("C")) {
                this.score = 55;
            }else if(score.getTotalScore().equals("D+")) {
                this.score = 50;
            }else if(score.getTotalScore().equals("D")) {
                this.score = 45;
            }else if(score.getTotalScore().equals("F")) {
                this.score = 40;
            }
        }
    }
}
