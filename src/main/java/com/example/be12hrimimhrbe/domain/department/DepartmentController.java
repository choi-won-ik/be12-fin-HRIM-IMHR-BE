package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
