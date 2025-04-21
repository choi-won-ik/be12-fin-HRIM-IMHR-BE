package com.example.be12hrimimhrbe.domain.partner.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
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

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class PartnerResponse {
        private Long partnerIdx;
        private String partnerName;
        private String type;

        public static PartnerResponse fromEntity(Partner partner) {
            return PartnerResponse.builder()
                    .partnerIdx(partner.getIdx())
                    .partnerName(partner.getName())
                    .type(partner.getType())
                    .build();
        }
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PartnerRequest {
        private Long idx;
        private String name;
        private String type;
        private String companyCode;
        private Long partnerIdx;

        public Partner toEntity(Company company) {
            return Partner.builder()
                    .idx(idx)
                    .name(name)
                    .type(type)
                    .companyCode(companyCode)
                    .partnerIdx(partnerIdx)
                    .mainCompany(company)
                    .build();
        }
    }


    // 모든 회사 조회 리스트
    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AllCompanyListResponse {
        private String type;
        private Long companyIdx;
        private String companyName;
        private String companyCode;
    }
}
