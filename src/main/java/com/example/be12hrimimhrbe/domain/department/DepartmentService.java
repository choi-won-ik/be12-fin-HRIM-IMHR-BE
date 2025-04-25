package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        double avgtotal = avgG + avgE + avgS;

        DepartmentDto.DepartmentScoreResponse response = DepartmentDto.DepartmentScoreResponse.fromEntity(department, avgE, avgS, avgG, avgtotal);
        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_MONTH_SCORE_SUCCESS, response);
    }


}