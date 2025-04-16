package com.example.be12hrimimhrbe.domain.department;

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

import java.util.List;

@RequiredArgsConstructor
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;

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
}
