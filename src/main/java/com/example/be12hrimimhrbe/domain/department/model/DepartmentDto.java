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
        private List<Department> departments;
        public static DepartmentListResponse fromDepartments(List<Department> departments){
            return new DepartmentListResponse(departments);
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CDRequest {
        private List<CreateRequest> createRequests;
        private List<DeleteRequest> deleteRequests;
        public List<Department> toCreateEntity(Company company) {
            List<Department> departments = new ArrayList<>();
            for (CreateRequest createRequest : createRequests) {
                departments.add(Department.builder()
                                .name(createRequest.name)
                                .company(company)
                        .build()
                );
            }
            return departments;
        }
        public List<Department> toDeleteEntity() {
            List<Department> departments = new ArrayList<>();
            for (DeleteRequest deleteRequest : deleteRequests) {
                departments.add(Department.builder().idx(deleteRequest.idx).build());
            }
            return departments;
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CreateRequest{
        private String name;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DeleteRequest{
        private Long idx;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class DepartmentInfoResponse {
        private Long idx;
        private String name;
        private Long companyIdx;
        private String companyName;
        public static DepartmentInfoResponse fromEntity(Department department) {
            return DepartmentInfoResponse.builder()
                    .idx(department.getIdx())
                    .name(department.getName())
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


        public static DepartmentScoreResponse fromEntity(Department department, double EScore, double GScore, double SScore) {
            return DepartmentScoreResponse.builder()
                    .idx(department.getIdx())
                    .departmentName(department.getName())
                    .departmentEScore(EScore)
                    .departmentSScore(SScore)
                    .departmentGScore(GScore)
                    .build();
        }
    }
}





//// 3. 활동 필터링 및 점수 계산
//Map<Long, int[]> memberScoreMap = companyMemberIdxs.stream()
//        .flatMap(idx -> activityRepository.findAllByMemberIdx(idx).stream())
//        .filter(a -> a.getCreatedAt().getYear() == year && a.getCreatedAt().getMonthValue() == month)
//        .collect(Collectors.groupingBy(
//                a -> a.getMember().getIdx(),
//                Collectors.reducing(
//                        new int[3], // [E, S, G]
//                        a -> {
//                            int[] score = new int[3];
//                            if (a.getType() != null) {
//                                switch (a.getType()) {
//                                    case VOLUNTEER, DONATION -> score[0] = a.getScore(); // E
//                                }
//                            }
//                            if (a.getEducationType() != null) {
//                                switch (a.getEducationType()) {
//                                    case ENVIRONMENTAL_EDUCATION -> score[0] = a.getScore(); // E
//                                    case SOCIAL_EDUCATION -> score[1] = a.getScore(); // S
//                                    case GOVERNANCE_EDUCATION -> score[2] = a.getScore(); // G
//                                }
//                            }
//                            return score;
//                        },
//                        (a1, a2) -> new int[]{
//                                a1[0] + a2[0],
//                                a1[1] + a2[1],
//                                a1[2] + a2[2]
//                        }
//                )
//        ));
//
//List<MemberDto.MemberScoreResponse> top3 = memberScoreMap.entrySet().stream()
//        .sorted((e1, e2) -> {
//            int total1 = Arrays.stream(e1.getValue()).sum();
//            int total2 = Arrays.stream(e2.getValue()).sum();
//            return Integer.compare(total2, total1); // 내림차순
//        })
//        .limit(3)
//        .map(entry -> {
//            Long memberId = entry.getKey();
//            int[] scoreArr = entry.getValue();
//            Member member1 = memberMap.get(memberId);
//            return MemberDto.MemberScoreResponse.from(member1, scoreArr[0], scoreArr[1], scoreArr[2]);
//        })
//        .collect(Collectors.toList());