package com.example.be12hrimimhrbe.domain.score;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import com.example.be12hrimimhrbe.domain.score.model.ScoreDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score")
@Tag(name = "주문 기능")
public class ScoreController {

    @PostMapping("/dashboard")
    @Operation(summary = "대쉬보드", description = "대쉬보드 실행 기능입니다.")
    public ResponseEntity<BaseResponse<ScoreDto.DashBoardRsp>> dashboard(@RequestBody ScoreDto.DashBoardReq dto) {

        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.MY_COMPANY_SUCCESS,new ScoreDto.DashBoardRsp()));
    }
}