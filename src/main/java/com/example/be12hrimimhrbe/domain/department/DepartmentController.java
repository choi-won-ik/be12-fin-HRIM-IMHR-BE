package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @PostMapping("/update")
    public ResponseEntity<BaseResponse<String>> update(@RequestBody DepartmentDto.CDRequest dto,
                                                       @AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(departmentService.updateElements(dto, member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<DepartmentDto.DepartmentListResponse>> list(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(departmentService.getList(member.getMember()));
    }

    @GetMapping("/month")
    @Operation(summary = "각 부서의 월별 대시보드 조회", description = "각 부서의 월별 대시보드를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<DepartmentDto.DepartmentScoreResponse>> monthDepartment (
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam(required = false) Long departmentIdx,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(departmentService.monthDepartment(member.getMember(), departmentIdx, year, month));
    }
}
