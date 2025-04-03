package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.CampaignDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
public class CampaignController {
    @GetMapping("/mycampaign")
    public ResponseEntity<BaseResponse<CampaignDto.CampaignListResponse>> getCampaigns(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
