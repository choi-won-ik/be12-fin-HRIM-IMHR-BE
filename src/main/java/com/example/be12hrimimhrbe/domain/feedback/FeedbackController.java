package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @GetMapping("/template")
    public ResponseEntity<BaseResponse<FeedbackDto.FeedbackTemplateResponse>> getFeedbackTemplate(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/answer")
    public ResponseEntity<BaseResponse<String>> feedbackAnswer(@AuthenticationPrincipal Member member,
                                                                @RequestBody FeedbackDto.FeedbackAnswerRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
