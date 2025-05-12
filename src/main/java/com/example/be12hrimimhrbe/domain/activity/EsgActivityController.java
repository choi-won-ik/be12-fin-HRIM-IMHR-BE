package com.example.be12hrimimhrbe.domain.activity;


import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/esg_activity")
@RequiredArgsConstructor
public class EsgActivityController {
    private final EsgActivityService esgActivityService;

    @PostMapping("/submit")
    public ResponseEntity<BaseResponse<String>> submit(@RequestBody EsgActivityDto.ActivityRequest dto,
                                                       @AuthenticationPrincipal CustomUserDetails member) {
        BaseResponse<String> response = esgActivityService.createActivity(dto, member);
        if(!response.getIsSuccess())
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok().body(response);
    }
}
