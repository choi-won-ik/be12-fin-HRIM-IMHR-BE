package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@Tag(name = "직원 활동내역 관리 기능")
public class ActivityController {
    @GetMapping("/myactivity")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityListResponse>> getMyActivity(@RequestBody ActivityDto.ActivityListRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/detail/{idx}")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityItemResponse>> getDetail(@PathVariable Long idx,
                                                                                    @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/regist")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityListResponse>> activityRegist(@RequestBody ActivityDto.ActivityListRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/ativityApproval/{idx}")
    @Operation(summary = "ESG활동 승인 페이지", description = "ESG활동 승인 페이지 입니다.")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityListResponse>> ativityApproval(@PathVariable Long idx) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PutMapping("/ativityApproval/agree")
    @Operation(summary = "ESG활동 승인", description = "직원 ESG활동을 승인 합니다.")
    public ResponseEntity<BaseResponse<Long>> ativityApprovalAgree() {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PutMapping("/ativityApproval/oppose")
    @Operation(summary = "ESG활동 반려", description = "직원 ESG활동을 반려 합니다.")
    public ResponseEntity<BaseResponse<Long>> ativityApprovalOppose() {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
