package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Tag(name = "피드백 관리 기능")
public class FeedbackController {

    @GetMapping("/template")
    @Operation(summary = "피드백 양식 조회", description = "피드백 양식 조회 기능입니다.")
    public ResponseEntity<BaseResponse<FeedbackDto.FeedbackTemplateResponse>> getFeedbackTemplate(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/answer")
    @Operation(summary = "피드백 응답 작성", description = "피드백 양식에 맞춰 응답을 작성하여 기록하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> feedbackAnswer(@AuthenticationPrincipal Member member,
                                                                @RequestBody FeedbackDto.FeedbackAnswerRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/modify")
    @Operation(summary = "피드백 양식 수정", description = "피드백 양식을 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> modifyFeedback(@AuthenticationPrincipal Member member,
                                                               @RequestBody FeedbackDto.FeedbackModifyRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
