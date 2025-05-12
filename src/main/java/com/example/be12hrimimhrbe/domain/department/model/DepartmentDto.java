package com.example.be12hrimimhrbe.domain.department.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentListResponse{
        private List<DepartmentInfoResponse> departments;
        public static DepartmentListResponse fromDepartments(List<Department> departments){
            return new DepartmentListResponse(departments.stream().map(DepartmentInfoResponse::fromEntity).toList());
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CDRequest {
        private List<CreateRequest> createRequests;
        public List<Department> toCreateEntity(Company company) {
            List<Department> departments = new ArrayList<>();
            for (CreateRequest createRequest : createRequests) {
                departments.add(Department.builder()
                                .name(createRequest.name)
                                .targetEScore(createRequest.targetEScore)
                                .targetSScore(createRequest.targetSScore)
                                .targetGScore(createRequest.targetGScore)
                                .is_deleted(false)
                                .company(company)
                        .build()
                );
            }
            return departments;
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CreateRequest{
        private String name;
        private int targetEScore;
        private int targetSScore;
        private int targetGScore;
        private boolean is_delete;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentInfoResponse {
        private Long idx;
        private String name;
        private int targetEScore;
        private int targetSScore;
        private int targetGScore;
        private Long companyIdx;
        private String companyName;
        public static DepartmentInfoResponse fromEntity(Department department) {
            return DepartmentInfoResponse.builder()
                    .idx(department.getIdx())
                    .name(department.getName())
                    .targetEScore(department.getTargetEScore())
                    .targetGScore(department.getTargetGScore())
                    .targetSScore(department.getTargetSScore())
                    .companyIdx(department.getCompany().getIdx())
                    .companyName(department.getCompany().getName())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentScoreResponse {
        private Long idx;
        private String departmentName;
        private double departmentEScore;
        private double departmentGScore;
        private double departmentSScore;
        private double departmentTotalScore;


        public static DepartmentScoreResponse fromEntity(Department department, double EScore, double GScore, double SScore, double totalScore) {
            return DepartmentScoreResponse.builder()
                    .idx(department.getIdx())
                    .departmentName(department.getName())
                    .departmentEScore(EScore)
                    .departmentSScore(SScore)
                    .departmentGScore(GScore)
                    .departmentTotalScore(totalScore)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleDepartmentDto {
        private Long idx;
        private String name;
        private double eScore;
        private double sScore;
        private double gScore;

        public static SimpleDepartmentDto fromEntity(Department dept, double eScore, double sScore, double gScore) {
            return SimpleDepartmentDto.builder()
                    .idx(dept.getIdx())
                    .name(dept.getName())
                    .eScore(eScore)
                    .sScore(sScore)
                    .gScore(gScore)
                    .build();
        }
    }

}