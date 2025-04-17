package com.example.be12hrimimhrbe.domain.feedback;

import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Tag(name = "피드백 관리 기능")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final MemberRepository memberRepository;

    @GetMapping("/template")
    @Operation(summary = "피드백 양식 조회", description = "피드백 양식 조회 기능입니다.")
    public ResponseEntity<BaseResponse<FeedbackDto.FeedbackTemplateResponse>> getFeedbackTemplate(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(feedbackService.getFeedbackTemplate(member.getMember()));
    }

    @PostMapping("/answer/{toMemberId}")
    @Operation(summary = "피드백 응답 작성", description = "피드백 양식에 맞춰 응답을 작성하여 기록하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> feedbackAnswer(@AuthenticationPrincipal Member from,
                                                             @PathVariable Long toMemberId,
                                                             @RequestBody FeedbackDto.FeedbackAnswerRequest dto) {
        Member to = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return ResponseEntity.ok().body(feedbackService.submitFeedback(from, to, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modify")
    @Operation(summary = "피드백 양식 수정", description = "피드백 양식을 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> modifyFeedback(@AuthenticationPrincipal CustomUserDetails member,
                                                             @RequestBody FeedbackDto.FeedbackModifyRequest dto) {
        return ResponseEntity.ok().body(feedbackService.modifyFeedbackTemplate(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "피드백 양식 생성", description = "피드백 양식을 생성하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> createFeedback(@AuthenticationPrincipal CustomUserDetails member,
                                                               @RequestBody FeedbackDto.FeedbackModifyRequest dto) {
        return ResponseEntity.ok().body(feedbackService.createFeedbackTemplate(member.getMember(), dto));
    }
}
