package com.example.be12hrimimhrbe.domain.partner.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PartnerDto {

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PartnerListResp {
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
        public static PartnerListResp fromEntity(Partner partner) {
            return PartnerListResp.builder()
            .companyName(partner.getName())
            .environmentScore(partner.getScore().getEnvironmentScore())
            .socialScore(partner.getScore().getSocialScore())
            .governanceScore(partner.getScore().getGovernanceScore())
            .totalScore(partner.getScore().getTotalScore())
            .build();
        }
    }
}
