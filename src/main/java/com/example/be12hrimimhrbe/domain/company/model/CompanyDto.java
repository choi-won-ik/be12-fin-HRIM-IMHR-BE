package com.example.be12hrimimhrbe.domain.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CompanyDto {
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyListResponse {
        private Long idx;
        private String name;
        private boolean isMember;
        private boolean has_esg_data;

        public static CompanyListResponse of(Company company) {
            return builder()
                    .idx(company.getIdx())
                    .name(company.getName())
                    .isMember(company.isMember())
                    .has_esg_data(company.isHas_esg_data())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyResponse {
        private Long idx;
        private String name;
        private boolean isMember;
        private boolean has_esg_data;
        private String registrationNumber;
        private String imgUrl;
        private LocalDateTime createdAt;
        private String code;
    }
}
