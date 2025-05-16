package com.example.be12hrimimhrbe.domain.department;

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
    private final DepartmentScoreRepository departmentScoreRepository;

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

        department.setName(dto.getName());
        department.setTargetEScore(dto.getTargetEScore());
        department.setTargetGScore(dto.getTargetGScore());
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

    @Transactional(readOnly = true)
    public BaseResponse<DepartmentDto.DepartmentScoreResponse> monthDepartment(Long departmentIdx, int year, int month) {

        Object[] ds = (Object[]) departmentScoreRepository.findByDepartmentIdx(departmentIdx,year,month);
        Department d = (Department) ds[0];
        DepartmentScore dScore = (DepartmentScore) ds[1];

        DepartmentDto.DepartmentScoreResponse response = DepartmentDto.DepartmentScoreResponse.builder()
                .idx(departmentIdx)
                .departmentName(d.getName())
                .departmentEScore(dScore != null && dScore.getEnvironment() != null ? dScore.getEnvironment() : 0)
                .departmentGScore(dScore != null && dScore.getGovernance() != null ? dScore.getGovernance() : 0)
                .departmentSScore(dScore != null && dScore.getSocial() != null ? dScore.getSocial() : 0)
                .departmentTotalScore(dScore != null && dScore.getTotal() != null ? dScore.getTotal() : 0)
                .build();

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