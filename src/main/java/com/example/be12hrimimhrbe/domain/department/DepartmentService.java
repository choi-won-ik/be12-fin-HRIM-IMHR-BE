package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public BaseResponse<String> create(DepartmentDto.CDRequest dto, Member member) {
        Member nowmember = memberRepository.findById(member.getIdx()).orElse(null);
        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, "회원을 찾을 수 없습니다.");
        }

        List<Department> createDepartments = dto.toCreateEntity(nowmember.getCompany());
        departmentRepository.saveAll(createDepartments);

        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_CREATE_SUCCESS, "부서 생성 완료");
    }

    @Transactional
    public BaseResponse<String> delete(Long departmentIdx, CustomUserDetails customMember) {
        Member member = memberRepository.findById(customMember.getMember().getIdx()).orElse(null);
        if (member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, "회원을 찾을 수 없습니다.");
        }

        Department department = departmentRepository.findByIdx(departmentIdx);

        if (department != null) {
            department.setIs_deleted(true);
        }

        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_DELETE_SUCCESS, "부서 삭제 완료");
    }

    @Transactional
    public BaseResponse<String> update(DepartmentDto.DepartmentInfoResponse dto, Member member) {
        Member nowmember = memberRepository.findById(member.getIdx()).orElse(null);
        Department department = departmentRepository.findById(dto.getIdx()).orElse(null);

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        if (!member.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }
        if (department == null) {
            return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_DELETE_FAIL, "존재하지 않는 부서 입니다.");
        }

        department.setTargetEScore(dto.getTargetEScore());
        department.setTargetSScore(dto.getTargetSScore());
        department.setTargetGScore(dto.getTargetGScore());
        departmentRepository.save(department);

        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_UPDATE_SUCCESS, "부서 업데이트 완료");
    }

    public BaseResponse<DepartmentDto.DepartmentListResponse> getList(Member member) {
        member = memberRepository.findById(member.getIdx()).orElse(null);
        if (member == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }

        List<Department> departments = departmentRepository.findAllByCompany(member.getCompany()).stream()
                .filter(d -> !d.getIs_deleted())
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_RETRIEVE_SUCCESS,
                DepartmentDto.DepartmentListResponse.fromDepartments(departments));
    }

    // 하나의 APi만 호출할때에 주석처리 해야함
    @Transactional
    public BaseResponse<DepartmentDto.DepartmentScoreResponse> monthDepartment(Member member, Long departmentIdx, int year, int month) {

        Company mycompany = member.getCompany();

        List<Member> dMembers;
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

    @Transactional
    public BaseResponse<DepartmentDto.DepartmentScoreResponse> monthDepartment1(Member member, Long departmentIdx, int year, int month) {


        DepartmentDto.DepartmentScoreResponse response = null;
        return new BaseResponse<>(BaseResponseMessage.DEPARTMENT_MONTH_SCORE_SUCCESS, response);
    }


    public DepartmentScore score(Department item) {
        List<Member> members = item.getMembers();

        int environment=0;
        int social=0;
        int governance= 0;
        for (Member member : members) {
            environment+=member.getEScore();
            social+=member.getSScore();
            governance+=member.getGScore();
        }

        return DepartmentScore.builder()
                .company(item.getCompany())
                .department(item)
                .total((environment+social+governance)/3)
                .environment(environment)
                .governance(governance)
                .social(social)
                .year(LocalDateTime.now().getYear())
                .month(LocalDateTime.now().getMonthValue())
                .build();
    }
}