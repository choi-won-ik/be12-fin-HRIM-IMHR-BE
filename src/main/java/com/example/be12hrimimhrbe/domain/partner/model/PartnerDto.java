package com.example.be12hrimimhrbe.domain.partner.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class PartnerDto {
    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PartnerListResp {
        @Schema(description = "회사명", example = "한화시스템")
        private String companyName;
        @Schema(description = "esg 환경 점수", example = "55")
        private String environmentScore;
        @Schema(description = "esg 사회 점수", example = "73")
        private String socialScore;
        @Schema(description = "esg 지배구조 점수", example = "80")
        private String governanceScore;
        @Schema(description = "esg 총 점수", example = "74")
        private String totalScore;
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

    @Getter
    @AllArgsConstructor
    public static class PartnerPageResponse {
        private Page<PartnerDto.PartnerListResp> partners;
        private Long companyIdx;
    }

    @Data
    public static class PartnerListResponse {
        private Long companyIdx;
        private List<CompanyDto.CompanyListResponse> partnerList;
    }
}
