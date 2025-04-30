package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public BaseResponse<String> updateElements(DepartmentDto.CDRequest dto, CustomUserDetails customMember) {
        Member member = memberRepository.findById(customMember.getMember().getIdx()).orElse(null);
        if (member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        List<Department> createDepartments = dto.toCreateEntity(member.getCompany());
        List<Department> deleteDepartments = dto.toDeleteEntity();
        departmentRepository.saveAll(createDepartments);
        departmentRepository.deleteAllInBatch(deleteDepartments);
        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_UPDATE_SUCCESS, "부서 설정 완료");
    }

    public BaseResponse<DepartmentDto.DepartmentListResponse> getList(Member member) {
        member = memberRepository.findById(member.getIdx()).orElse(null);
        if (member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }
        List<Department> departments = departmentRepository.findAllByCompany(member.getCompany());
        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_RETRIEVE_SUCCESS,
                DepartmentDto.DepartmentListResponse.fromDepartments(departments));
    }

    // 하나의 APi만 호출할때에 주석처리 해야함
    @Transactional
    public BaseResponse<DepartmentDto.DepartmentScoreResponse> monthDepartment(Member member, Long departmentIdx, int year, int month) {

        Company mycompany = member.getCompany();

        List<Member> dMembers = new ArrayList<>();
        Department department;

        // 1. 해당 부서 소속 사원 목록 조회
        if (departmentIdx == null) {
            // 회사의 제일 작은 idx를 가진 부서 조회
            Long departId = departmentRepository.findFirstByCompanyOrderByIdxAsc(mycompany).getIdx();
            dMembers = memberRepository.findAllByDepartmentIdx(departId);
            department = departmentRepository.findByIdx(departId);
        } else {
            dMembers = memberRepository.findAllByDepartmentIdx(departmentIdx);
            department = departmentRepository.findByIdx(departmentIdx);
        }

        //부서 소속 총 사원 수
        int memberCount = dMembers.size();

        // E, S, G 점수 총 합
        int totalE = 0;
        int totalS = 0;
        int totalG = 0;

        // 사원별 모든 활동들의 점수들 계산
        for (Member m : dMembers) {
            // 각 사원의 활동 총조회
            List<Activity> activities = activityRepository.findAllByMember(m).stream()
                    .filter(a -> a.getCreatedAt().getYear() == year
                    && a.getCreatedAt().getMonthValue() == month)
                    .collect(Collectors.toList());

            // 활동 점수 계산
            for (Activity a : activities) {
                if (a.getType() != null) {
                    switch (a.getType()) {
                        case VOLUNTEER -> totalE += a.getScore(); // 환경
                        case DONATION -> totalE += a.getScore();
                    }
                }

                // EducationType이 null이 아닌 경우에만 switch
                if (a.getEducationType() != null) {
                    switch (a.getEducationType()) {
                        case ENVIRONMENTAL_EDUCATION -> totalE += a.getScore();
                        case SOCIAL_EDUCATION -> totalS += a.getScore();
                        case GOVERNANCE_EDUCATION -> totalG += a.getScore();
                    }
                }
            }
        }

        double avgE = memberCount > 0 ? (double) totalE / memberCount : 0.0;
        double avgS = memberCount > 0 ? (double) totalS / memberCount : 0.0;
        double avgG = memberCount > 0 ? (double) totalG / memberCount : 0.0;
        double avgtotal = (avgG + avgE + avgS) /3;

        DepartmentDto.DepartmentScoreResponse response = DepartmentDto.DepartmentScoreResponse.fromEntity(department, avgE, avgS, avgG, avgtotal);
        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_MONTH_SCORE_SUCCESS, response);
    }


}



// 삭제할 것들
///@Transactional
//public BaseResponse<CompanyDto.CompanyYearResponse> monthDashboard(Member member, int year, int month) {
//    Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
//    Company company = companyRepository.findByIdx(myCompanyIdx);
//
//    // 1. 기업에 해당하는 모든 사원 Idx 조회
//    List<Long> companyMemberIdxs = memberRepository.findAllByCompanyIdx(myCompanyIdx)
//            .stream().map(m -> m.getIdx()).collect(Collectors.toList());
//
//    // 2. 모든 사원 ID -> 사원 객체 Map (이름 등 매핑용)
//    Map<Long, Member> memberMap = memberRepository.findAllByCompanyIdx(myCompanyIdx).stream()
//            .collect(Collectors.toMap(Member::getIdx, m -> m));
//
//    // 3. 모든 사원 활동 필터링 및 점수 계산
//    Map<Long, int[]> memberScoreMap = companyMemberIdxs.stream()
//            .flatMap(idx -> activityRepository.findAllByMemberIdx(idx).stream())
//            .filter(a -> a.getCreatedAt().getYear() == year && a.getCreatedAt().getMonthValue() == month)
//            .collect(Collectors.groupingBy(
//                    a -> a.getMember().getIdx(),
//                    Collectors.reducing(
//                            new int[3], // [E, S, G]
//                            a -> {
//                                int[] score = new int[3];
//                                if (a.getType() != null) {
//                                    switch (a.getType()) {
//                                        case VOLUNTEER, DONATION -> score[0] = a.getScore(); // E
//                                    }
//                                }
//                                if (a.getEducationType() != null) {
//                                    switch (a.getEducationType()) {
//                                        case ENVIRONMENTAL_EDUCATION -> score[0] = a.getScore(); // E
//                                        case SOCIAL_EDUCATION -> score[1] = a.getScore(); // S
//                                        case GOVERNANCE_EDUCATION -> score[2] = a.getScore(); // G
//                                    }
//                                }
//                                return score;
//                            },
//                            (a1, a2) -> new int[]{
//                                    a1[0] + a2[0],
//                                    a1[1] + a2[1],
//                                    a1[2] + a2[2]
//                            }
//                    )
//            ));
//
//    //4. 총합 기준 정렬 후 상위 3명 뽑기
//    List<MemberDto.MemberScoreResponse> top3 = memberScoreMap.entrySet().stream()
//            .sorted((e1, e2) -> {
//                int total1 = Arrays.stream(e1.getValue()).sum();
//                int total2 = Arrays.stream(e2.getValue()).sum();
//                return Integer.compare(total2, total1); // 내림차순
//            })
//            .limit(3)
//            .map(entry -> {
//                Long memberId = entry.getKey();
//                int[] scoreArr = entry.getValue();
//                Member member1 = memberMap.get(memberId);
//                return MemberDto.MemberScoreResponse.from(member1, scoreArr[0], scoreArr[1], scoreArr[2]);
//            })
//            .collect(Collectors.toList());
//
//    // 회사에 소속되어 있는 부서 조회
//    List<DepartmentDto.SimpleDepartmentDto> departments = departmentRepository.findAllByCompany(company).stream()
//            .map(d -> DepartmentDto.SimpleDepartmentDto.fromEntity(d))
//            .collect(Collectors.toList());
//    // Dto 변환
//    CompanyDto.CompanyYearResponse response = CompanyDto.CompanyYearResponse.of(company, top3, departments);
//    return new BaseResponse<>(BaseResponseMessage.COMPANY_DEPARTMENT_MONTH_SUCCESS, response);
//}