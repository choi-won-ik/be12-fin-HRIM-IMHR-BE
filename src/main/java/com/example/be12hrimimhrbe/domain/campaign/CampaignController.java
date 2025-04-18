package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.CampaignDto;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaign")
@Tag(name = "캠페인 관리 기능")
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping("/register")
    @Operation(summary = "캠페인 사원 등록", description = "캠페인에 참가한 사원들을 추가하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<Long>>> register(
            @RequestBody CampaignDto.CampaignRequest request
    ) {
        List<Long> failedList = campaignService.register(request);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_CAMPAIGN_REGISTER_SUCCESS, failedList));
    }

//  캠페인 상세 조회도 가능
    @GetMapping("/memberList/{eventIdx}")
    @Operation(summary = "캠페인 참가 인원 조회", description = "캠페인에 참가한 사원을 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberShortResponse>>> memberList(
            @PathVariable Long eventIdx
    ) {
        List<MemberDto.MemberShortResponse> responses = campaignService.memberList(eventIdx);
        return ResponseEntity.ok().body(new BaseResponse<>(BaseResponseMessage.CALENDAR_CAMPAIGN_DETAIL_SUCCESS, responses));
    }

//  캠페인 수정
    @PutMapping("/update/{eventIdx}")
    @Operation(summary = "캠페인 수정", description = "등록했던 캠페인 참가 사원들을 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberShortResponse>>> update(
            @PathVariable Long eventIdx,
            @RequestBody CampaignDto.CampaignRequest dto
    ) {
        List<MemberDto.MemberShortResponse> responses = campaignService.update(eventIdx, dto);
        return ResponseEntity.ok().body(new BaseResponse<>(BaseResponseMessage.CALENDAR_CAMPAIGN_UPDATE_SUCCESS, responses));
    }
}
