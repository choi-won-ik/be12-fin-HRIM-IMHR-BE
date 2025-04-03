package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @PostMapping("/find-id")
    public ResponseEntity<BaseResponse<String>> findMemberId(@RequestBody MemberDto.FindIdRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(BaseResponseMessage.FIND_ID_SUCCESS, "ID 찾기 성공"));
    }

    @PostMapping("/find-pw")
    public ResponseEntity<BaseResponse<String>> findMemberPw(@RequestBody MemberDto.FindPWRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/reset-pw")
    public ResponseEntity<BaseResponse<String>> resetMemberPw(@RequestBody MemberDto.ResetPasswordRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
