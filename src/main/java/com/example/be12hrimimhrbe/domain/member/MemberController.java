package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member")
@Tag(name = "유저 관리 기능")
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

    @PostMapping("/myinfo")
    public ResponseEntity<BaseResponse<MemberDto.InfoResponse>> myinfo(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/myactivity/list")
    public ResponseEntity<BaseResponse<MemberDto.ActivityResponse>> myactivity(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/signup/personal")
    public ResponseEntity<BaseResponse<MemberDto.PersonalSignupResponse>> personalSignup(@RequestBody MemberDto.PersonalSignupRequest dto) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/signup/company")
    public ResponseEntity<BaseResponse<MemberDto.CompanySignupResponse>> companySignup(@RequestPart MemberDto.CompanySignupRequest dto,
                                                                                       @RequestPart MultipartFile file) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/list")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberShortResponse>>> allList(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @GetMapping("/reportDetail/{idx}")
    @Operation(summary = "리포트 상세보기", description = "리포트 상세보기 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.MemberReportDetailResp>> ReportDetail(
            @PathVariable int memberIdx, @RequestBody MemberDto.MemberReportReq dto
    ) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/reportList")
    @Operation(summary = "리포트 내역", description = "리포트 내역 보기 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberReportListResp>>> ReportList(
            @AuthenticationPrincipal Member member, @RequestBody MemberDto.MemberReportReq dto
    ) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }

    @PostMapping("/reportUserFind")
    @Operation(summary = "리포트 검색", description = "해당 유저의 리포트를 찾는 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberReportUserFindResp>>> ReportUserFind(
            @RequestBody MemberDto.MemberReportUserFindReq dto
    ) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, null));
    }
}
