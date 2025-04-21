package com.example.be12hrimimhrbe.domain.partner;

import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
    
@RestController
@RequestMapping("/partner")
@Tag(name = "협력사 관리 기능")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping("/{idx}")
    @Operation(summary = "협럭사 리스트", description = "협력사 리스트를 확인합니다.")
    public ResponseEntity<BaseResponse<List<PartnerDto.PartnerListResp>>> List(
            @Parameter(description = "협력사 IDX", example = "1")
            @PathVariable Long idx) {
        return ResponseEntity.ok().body(partnerService.getPartnerList(idx));
    }

    @GetMapping("/add/{companyIdx}")
    @Operation(summary = "협력사 추가", description = "협력사를 추가하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<PartnerDto.PartnerRequest>>> addPartner (
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long companyIdx,
            @RequestBody List<PartnerDto.PartnerRequest> dto
            ) {
        return ResponseEntity.ok().body(partnerService.addPartner(member.getMember(), companyIdx, dto));
    }
}