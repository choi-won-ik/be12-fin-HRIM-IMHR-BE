package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "부서 생성", description = "부서를 생성하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> create(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody DepartmentDto.CDRequest dto
    ) {
        return ResponseEntity.ok().body(departmentService.create(dto , member.getMember()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{departmentIdx}")
    @Operation(summary = "부서 삭제", description = "부서를 삭제하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> delete(
            @PathVariable Long departmentIdx,
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        return ResponseEntity.ok().body(departmentService.delete(departmentIdx, member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    @Operation(summary = "부서 수정", description = "부서를 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> update(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody DepartmentDto.DepartmentInfoResponse dto
            ) {
        return ResponseEntity.ok().body(departmentService.update(dto, member.getMember()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<DepartmentDto.DepartmentListResponse>> list(
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        return ResponseEntity.ok().body(departmentService.getList(member.getMember()));
    }

    // 하나의 APi만 호출할때에 주석처리 해야함
    @GetMapping("/month")
    @Operation(summary = "각 부서의 월별 대시보드 조회", description = "각 부서의 월별 대시보드를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<DepartmentDto.DepartmentScoreResponse>> monthDepartment (
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam Long departmentIdx,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(departmentService.monthDepartment(departmentIdx, year, month));
    }
}
