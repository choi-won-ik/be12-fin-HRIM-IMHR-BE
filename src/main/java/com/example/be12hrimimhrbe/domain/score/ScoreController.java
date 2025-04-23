package com.example.be12hrimimhrbe.domain.score;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import com.example.be12hrimimhrbe.domain.score.model.ScoreDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/score")
@Tag(name = "주문 기능")
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/dashboard")
    @Operation(summary = "회사 대쉬보드", description = "대쉬보드 실행 기능입니다.")
    public ResponseEntity<BaseResponse<ScoreDto.DashBoardRsp>> dashboard(@AuthenticationPrincipal CustomUserDetails member) {
        scoreService.dashboard(member.getMember());
        return ResponseEntity.ok().body(scoreService.dashboard(member.getMember()));
    }


}