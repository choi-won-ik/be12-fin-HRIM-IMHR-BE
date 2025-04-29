package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {
    private final ActivityService activityService;

    @PostMapping("/regist")
    @Operation(summary = "ESG교육 등록", description = "ESG교육 등록 기능입니다.")
    public ResponseEntity<BaseResponse<Activity>> activityRegist(
            @RequestPart("dto") @Valid ActivityDto.ActivityRegistReq dto, @RequestPart(value = "file", required = false) MultipartFile imgFile
            , @AuthenticationPrincipal CustomUserDetails member
    ) {
        return ResponseEntity.ok().body(activityService.Regist(dto, imgFile, member.getMember()));
    }
}
