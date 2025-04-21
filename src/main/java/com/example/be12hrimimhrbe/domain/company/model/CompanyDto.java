package com.example.be12hrimimhrbe.domain.company.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CompanyDto {
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ESG_CompanyResponse {
        @Schema(description = "ESG_Company 고유 식별 값", example = "1")
        private Long idx;
        @Schema(description = "ESG_Company 이름", example = "한화시스템")
        private String company_name;
        @Schema(description = "ESG_Company 고유 코드", example = "294ty8hfue")
        private String company_code;

        public static ESG_CompanyResponse of(ESGCompany company) {
            return builder()
                    .idx(company.getIdx())
                    .company_name(company.getCompany_name())
                    .company_code(company.getCompany_code())
                    .build();
        }
    }
}
