package com.example.be12hrimimhrbe.domain.partner;

import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}