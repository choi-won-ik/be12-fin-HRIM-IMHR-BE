package com.example.be12hrimimhrbe.domain.activity;


import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/esg_activity")
@RequiredArgsConstructor
public class EsgActivityController {
    private final EsgActivityService esgActivityService;

    @PostMapping("/submit")
    public ResponseEntity<BaseResponse<String>> submit(@RequestPart EsgActivityDto.ActivityRequest dto,
                                                       MultipartHttpServletRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails member) {
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
}
