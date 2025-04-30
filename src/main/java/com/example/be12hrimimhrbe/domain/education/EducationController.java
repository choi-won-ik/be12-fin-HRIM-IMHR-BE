package com.example.be12hrimimhrbe.domain.education;

import com.example.be12hrimimhrbe.domain.activity.ActivityService;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.education.model.EducationDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {
    private final ActivityService activityService;
    private final EducationService educationService;

    @PostMapping("/regist")
    @Operation(summary = "ESG교육 등록", description = "ESG교육 등록 기능입니다.")
    public ResponseEntity<BaseResponse<Activity>> activityRegist(
            @RequestPart("dto") @Valid ActivityDto.ActivityRegistReq dto, @RequestPart(value = "file", required = false) MultipartFile imgFile
            , @AuthenticationPrincipal CustomUserDetails member
    ) {
        return ResponseEntity.ok().body(activityService.Regist(dto, imgFile, member.getMember()));
    }

    @GetMapping("/activityList")
    @Operation(summary = "ESG활동 내역 조회", description = "ESG활동 내역을 조회하는 기능 입니다.")
    public ResponseEntity<BaseResponse<EducationDto.PageEducationListResp>> activityList(
            @AuthenticationPrincipal CustomUserDetails member, int page
    ) {
        return ResponseEntity.ok().body(educationService.activityList(member.getMember(), page, 5));
    }

    @GetMapping("/activitySearch")
    @Operation(summary = "ESG활동 내역 검색", description = "ESG활동 내역을 검색하는 기능 입니다.")
    public ResponseEntity<BaseResponse<EducationDto.PageEducationListResp>> activitySearch(
            @AuthenticationPrincipal CustomUserDetails member, int page, String search
    ) {
        return ResponseEntity.ok().body(educationService.activitySearch(member.getMember(), page, 5,search));
    }
}
