package com.example.be12hrimimhrbe.domain.activitySubject;

import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubjectDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody List<ActivitySubjectDto.ActivitySubjectRequest> dto
    ) {
        return ResponseEntity.ok().body(activitySubjectService.create(dto, member.getMember()));
    }

    @PostMapping("/search")
    @Operation(summary = "생성했던 활동 주제 양식 리스트 조회 기능", description = "생성했던 활동 주제 양식 리스트를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<ActivitySubjectDto.ActivitySubjectResponse>>> search(
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        return ResponseEntity.ok().body(activitySubjectService.search(member.getMember()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    @Operation(summary = "생성했던 활동 주제 양식 수정 기능", description = "생성했던 활동 주제 양식 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> update(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody ActivitySubjectDto.ActivitySubjectResponse dto
    ) {
        return ResponseEntity.ok().body(activitySubjectService.update(member.getMember(), dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "활동 주제 양식 삭제 기능", description = "활동 주제 양식 삭제하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> delete(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable String id
    ) {
        return ResponseEntity.ok().body(activitySubjectService.delete(member.getMember(), id));
    }
}
