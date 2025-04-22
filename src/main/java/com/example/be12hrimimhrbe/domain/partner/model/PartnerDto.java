package com.example.be12hrimimhrbe.domain.partner.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.score.model.Score;
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
        public static PartnerListResp fromEntity(Company company, Score score) {
            return PartnerListResp.builder()
            .companyName(company.getName())
            .environmentScore(score.getEnvironmentScore())
            .socialScore(score.getSocialScore())
            .governanceScore(score.getGovernanceScore())
            .totalScore(score.getTotalScore())
            .build();
        }
    }

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class PartnerResponse {
        private Long partnerIdx;
        private String partnerName;
        private String companyCode;

        public static PartnerResponse fromEntity(Partner partner) {
            return PartnerResponse.builder()
                    .partnerIdx(partner.getIdx())
                    .partnerName(partner.getPartnerCompany().getName())
                    .companyCode(partner.getPartnerCompany().getCode())
                    .build();
        }
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PartnerRequest {
        private Long idx;

        public Partner toEntity(Company mainCompany, Company partnerCompany) {
            return Partner.builder()
                    .idx(idx)
                    .mainCompany(mainCompany)
                    .partnerCompany(partnerCompany)
                    .build();
        }
    }
}
