package com.example.be12hrimimhrbe.domain.partner;

import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/pageList")
    @Operation(summary = "협럭사 리스트", description = "협력사 리스트를 확인합니다.")
    public ResponseEntity<BaseResponse<PartnerDto.PartnerPageResponse>> partnerList(
            @Parameter(description = "협력사 IDX", example = "1")
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(partnerService.pageList(member.getMember(), pageable));
    }

    @PostMapping("/add")
    @Operation(summary = "협력사 추가", description = "협력사를 추가하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<Partner>>> addPartner (
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestBody PartnerDto.PartnerListResponse response
            ) {
        return ResponseEntity.ok().body(partnerService.addPartner(member.getMember(), response.getCompanyIdx(), response.getPartnerList()));
    }

    @DeleteMapping("/delete/{partnerIdx}")
    @Operation(summary = "협력사 제거", description = "협력사를 삭제하는 기능입니다.")
    public ResponseEntity<BaseResponse<Boolean>> deletePartner (
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long partnerIdx
    ) {
        boolean isDeleted = partnerService.deletePartner(member.getMember(), partnerIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.PARTNER_DELETE_SUCCESS, isDeleted));
    }
}