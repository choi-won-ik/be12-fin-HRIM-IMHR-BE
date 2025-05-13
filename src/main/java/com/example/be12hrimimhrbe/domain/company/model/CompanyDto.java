package com.example.be12hrimimhrbe.domain.company.model;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import lombok.*;

import java.util.List;

public class CompanyDto {
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyListResponse {
        private Long idx;
        private String name;
        private boolean isMember;
        private boolean has_esg_data;
        private String eScore;
        private String sScore;
        private String gScore;

        public static CompanyListResponse of(Company company, Score score) {
            return builder()
                    .idx(company.getIdx())
                    .name(company.getName())
                    .isMember(company.isMember())
                    .has_esg_data(company.isHas_esg_data())
                    .eScore(score.getSocialScore())
                    .sScore(score.getSocialScore())
                    .gScore(score.getGovernanceScore())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyYearResponse {
        private Long idx;
        private String companyName;

        // 사원 1, 2, 3 등을 담는 리스트
        private List<MemberDto.MemberScoreResponse> memberScores;
        // 소속회사의 부서들
        private List<DepartmentDto.SimpleDepartmentDto> departments;

        public static CompanyYearResponse of (Company company, List<MemberDto.MemberScoreResponse> member, List<DepartmentDto.SimpleDepartmentDto> departmentList) {
            return CompanyYearResponse.builder()
                    .idx(company.getIdx())
                    .companyName(company.getName())
                    .memberScores(member.stream().toList())
                    .departments(departmentList.stream().toList())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanyResponse {
        private Long idx;
        private String name;
        private int targetEScore;
        private int targetSScore;
        private int targetGScore;

        public static CompanyResponse of (Company company) {
            return CompanyResponse.builder()
                    .idx(company.getIdx())
                    .name(company.getName())
                    .targetEScore(company.getTargetEScore())
                    .targetSScore(company.getTargetSScore())
                    .targetGScore(company.getTargetGScore())
                    .build();
        }
    }
}