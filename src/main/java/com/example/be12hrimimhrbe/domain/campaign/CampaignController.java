package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.CampaignDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
@Tag(name = "캠페인 관리 기능")
public class CampaignController {
    @GetMapping("/mycampaign")
    public ResponseEntity<BaseResponse<CampaignDto.CampaignListResponse>> getCampaigns(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
    @GetMapping("/detail/{idx}")
    public ResponseEntity<BaseResponse<CampaignDto.CampaignItemResponse>> getCampaignItem(@PathVariable Integer idx,
                                                                                          @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/campaign/{idx}")
    @Operation(summary = "캠페인 상세", description = "캠페인 상세 페이지 입니다.")
    public ResponseEntity<BaseResponse<CampaignDto.CampaignDetailResp>> campaignDetail(@PathVariable Long idx) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
