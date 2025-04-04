package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @GetMapping("/myactivity")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityListResponse>> getMyActivity(@RequestBody ActivityDto.ActivityListRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/detail/{idx}")
    public ResponseEntity<BaseResponse<ActivityDto.ActivityItemResponse>> getDetail(@PathVariable Long idx,
                                                                                    @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
