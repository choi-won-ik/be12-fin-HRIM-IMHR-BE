package com.example.be12hrimimhrbe.domain.activitySubject;

import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubjectDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activitySubject")
public class ActivitySubjectController {
    private final ActivitySubjectService activitySubjectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "활동 주제별 입력 양식 생성 기능", description = "활동 주제별 입력 양식을 생성하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> create(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody ActivitySubjectDto.ActivitySubjectRequest dto
    ) {
        return ResponseEntity.ok().body(activitySubjectService.create(dto, member.getMember()));
    }
}
