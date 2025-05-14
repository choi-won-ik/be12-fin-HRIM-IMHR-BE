package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivity;
import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/esg_activity")
@RequiredArgsConstructor
public class EsgActivityController {
    private final EsgActivityService esgActivityService;

    @PostMapping("/submit")
    @Operation(summary = "내 활동 등록", description = "내 활동 등록 하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> submit(
            @RequestPart("dto") EsgActivityDto.ActivityRequest dto,
            MultipartHttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails member
    ) {
        // 1) 모든 파일 파트 얻어오기
        Map<String, MultipartFile> fileMap = request.getFileMap();

        // 2) dto 파트(또는 빈 파일) 걸러내기
        List<MultipartFile> files = fileMap.entrySet().stream()
                .filter(e -> !"dto".equals(e.getKey()))                 // dto 파트 제외
                .map(Map.Entry::getValue)
                .filter(f -> !f.isEmpty())                              // 빈 파일이 있을 경우 제외
                .collect(Collectors.toList());
        BaseResponse<String> response = esgActivityService.createActivity(dto, member, files);
        if(!response.getIsSuccess())
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/pageList")
    @Operation(summary = "내 활동 리스트 조회", description = "내가 한 ESG 활동들의 리스트를 조회하는기능입니다.")
    public ResponseEntity<BaseResponse<Page<EsgActivityDto.ActivityResponse>>> listSearch(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam Long myIdx,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok().body(esgActivityService.listSearch(member.getMember(), myIdx, pageable, keyword));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "활동 상세 조회", description = "활동 내역 상세 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<EsgActivity>> detail(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable String id
    ) {
        return ResponseEntity.ok().body(esgActivityService.detail(member.getMember(), id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "활동 삭제", description = "활동 내역을 삭제하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> delete(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable String id
    ) {
        return ResponseEntity.ok().body(esgActivityService.delete(member.getMember(), id));
    }

    @GetMapping("/ativityApproval/agree/{id}")
    @Operation(summary = "ESG활동 승인", description = "직원 ESG활동을 승인 합니다.")
    public ResponseEntity<BaseResponse<String>> approvalAgree(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable String id
    ){
        return ResponseEntity.ok().body(esgActivityService.approvalAgree(member.getMember(), id));
    }

    @GetMapping("/ativityApproval/oppose/{id}")
    @Operation(summary = "ESG활동 반려", description = "직원 ESG활동을 반려 합니다.")
    public ResponseEntity<BaseResponse<String>> approvalOppose(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable String id
    ){
        return ResponseEntity.ok().body(esgActivityService.approvalOppose(member.getMember(), id));
    }
}
