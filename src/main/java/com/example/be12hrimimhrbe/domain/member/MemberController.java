package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Enumeration;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
@Tag(name = "유저 관리 기능")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/find-id")
    @Operation(summary = "ID 찾기", description = "이름, 이메일을 입력하여 이메일로 아이디를 전송하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> findMemberId(@RequestBody MemberDto.FindIdRequest dto) {

        return ResponseEntity.ok().body(memberService.findId(dto));
    }

    @PostMapping("/find-pw")
    @Operation(summary = "비밀번호 찾기", description = "ID, 이메일을 입력하여 이메일로 비밀번호 재설정 링크를 전송하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> findMemberPw(@RequestBody MemberDto.FindPWRequest dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(memberService.findPassword(dto, request.getHeader("Origin")));
    }

    @PostMapping("/reset-pw")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정 합니다.")
    public ResponseEntity<BaseResponse<String>> resetMemberPw(@RequestBody MemberDto.ResetPasswordRequest dto,
                                                              @AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(memberService.passwordReset(dto, member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/info/{idx}")
    @Operation(summary = "회원 상세 조회", description = "관리자가 회원을 관리하기 위해 상세 정보를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.InfoDetailResponse>> detailInfo(@PathVariable Long idx,
                                                                                 @AuthenticationPrincipal CustomUserDetails member
                                                                                 ) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.getStaffDetail(idx));
    }

    @GetMapping("/detail/shortinfo/{idx}")
    @Operation(summary = "회원 조회", description = "회원의 정보를 간단하게 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.MemberShortResponse>> detailShortInfo(@PathVariable Long idx,
                                                                                 @AuthenticationPrincipal CustomUserDetails member
    ) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.getShortDetail(idx));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/detail/modify/{idx}")
    @Operation(summary = "회원정보 수정", description = "회원 정보를 수정합니다.")
    public ResponseEntity<BaseResponse<String>> detailModify(@PathVariable Long idx,
                                                             @AuthenticationPrincipal CustomUserDetails member,
                                                             @RequestBody MemberDto.InfoDetailRequest dto) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.modifyMember(idx, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{idx}")
    @Operation(summary = "회원 탈퇴", description = "관리자가 회원을 탈퇴 처리하는 기능입니다.")
    public ResponseEntity<BaseResponse<String>> deleteMember(@PathVariable Long idx,
                                                             @AuthenticationPrincipal CustomUserDetails member) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.deleteMember(idx));
    }

    @PostMapping("/myinfo")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.InfoResponse>> myinfo(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(memberService.getMyInfo(member));
    }

    @PostMapping("/myactivity/list")
    @Operation(summary = "내 ESG 관련 활동 조회", description = "캠페인, 교육을 포함한 내 ESG 관련 활동을 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.ActivityResponse>> myactivity(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(memberService.getMyActivity(member));
    }

    @PostMapping("/signup/personal")
    @Operation(summary = "개인 회원 가입", description = "임직원 가입용 회원 가입 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.PersonalSignupResponse>> personalSignup(@RequestBody MemberDto.PersonalSignupRequest dto) {
        return ResponseEntity.ok().body(memberService.personalSignup(dto));
    }

    @PostMapping("/signup/company")
    @Operation(summary = "기업 회원 가입", description = "기업 가입용 회원 가입 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.CompanySignupResponse>> companySignup(@RequestPart MemberDto.CompanySignupRequest dto,
                                                                                       @RequestPart MultipartFile file) {
        return ResponseEntity.ok().body(memberService.companySignup(dto, file));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/{idx}")
    @Operation(summary = "회원 승인", description = "관리자가 임직원 회원의 가입을 승인합니다.")
    public ResponseEntity<BaseResponse<String>> approveMember(@PathVariable Long idx, @AuthenticationPrincipal CustomUserDetails member) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.approveMember(idx));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/{idx}")
    @Operation(summary = "회원 반려", description = "관리자가 임직원 회원의 가입을 반려합니다.")
    public ResponseEntity<BaseResponse<String>> rejectMember(@PathVariable Long idx, @AuthenticationPrincipal CustomUserDetails member) {
        if(!memberService.isSameCompany(member.getMember().getIdx(), idx)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403))
                    .body(new BaseResponse<>(BaseResponseMessage.FORBIDDEN, null));
        }
        return ResponseEntity.ok().body(memberService.rejectMember(idx));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/list")
    @Operation(summary = "회원 내역", description = "회원 리스트를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberShortResponse>>> allList(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(memberService.getMemberAll(member.getMember()));
    }

    @PostMapping("/list/partial")
    @Operation(summary = "접근 가능한 회원 내역", description = "권한있는 회원 리스트를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<List<MemberDto.MemberShortResponse>>> partialList(@AuthenticationPrincipal CustomUserDetails member) {
        return ResponseEntity.ok().body(memberService.getMemberPartial(member.getMember(), member.getHrAuthoritySet()));
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


    @PostMapping("/info")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회하는 기능입니다.")
    public ResponseEntity<BaseResponse<MemberDto.TokenInfoResponse>> info(@AuthenticationPrincipal CustomUserDetails member) {
        MemberDto.TokenInfoResponse response = MemberDto.TokenInfoResponse
                                                            .fromMember(member.getMember(),
                                                                        member.getHrAuthoritySet().stream().toList());
        return ResponseEntity.ok().body(new BaseResponse<>(BaseResponseMessage.MYINFO_RETRIEVE_SUCCESS, response));
    }
}
