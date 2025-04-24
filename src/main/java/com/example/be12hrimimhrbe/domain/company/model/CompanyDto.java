package com.example.be12hrimimhrbe.domain.company.model;

import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    public static class CompanyYearResponse {
        private Long idx;
        private String companyName;

        // 사원 1, 2, 3 등을 담는 리스트
        private List<MemberDto.MemberScoreResponse> memberScores;
        // 소속회사의 부서들
        private List<Department> departments;

        public static CompanyYearResponse of (Company company, List<MemberDto.MemberScoreResponse> member, List<Department> departmentList) {
            return CompanyYearResponse.builder()
                    .idx(company.getIdx())
                    .companyName(company.getName())
                    .memberScores(member.stream().toList())
                    .departments(departmentList.stream().toList())
                    .build();
        }
    }
}