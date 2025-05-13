package com.example.be12hrimimhrbe.domain.activity;


import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/listsearch/{myIdx}")
    @Operation(summary = "내 활동 리스트 조회", description = "내가 한 ESG 활동들의 리스트를 조회하는기능입니다.")
    public ResponseEntity<BaseResponse<List<EsgActivityDto.ActivityResponse>>> listSearch(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long myIdx
    ) {
        return ResponseEntity.ok().body(esgActivityService.listSearch(member.getMember(), myIdx));
    }
}
