package com.example.be12hrimimhrbe.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum BaseResponseMessage {
    // ========================================================================================================================
    // 요청 성공, 실패, 내부 서버 오류
    REQUEST_SUCCESS(true, 200, "요청이 정상적으로 처리되었습니다"),
    REQUEST_FAIL(false, 404, "요청을 실패했습니다."),
    FORBIDDEN(false, 403, "금지된 요청입니다."),
    INTERNAL_SERVER_ERROR(false, 500, "내부 서버 오류"),

    // ========================================================================================================================
    //부서 기능(1000)
    DEPARTMENT_UPDATE_SUCCESS(true, 2200, "부서 설정에 성공했습니다."),
    DEPARTMENT_RETRIEVE_SUCCESS(true, 2200, "부서 조회에 성공했습니다."),

    // 회원 기능(2000)
    // 회원가입 2000
    LOGIN_SUCCESS(true, 2201, "로그인 되었습니다."),
    COMPANY_SIGNUP_SUCCESS(true, 2200, "기업 회원 가입 성공"),
    COMPANY_SIGNUP_NOT_FOUND_FILE(false, 2404, "파일을 찾지 못했습니다."),
    PERSONAL_SIGNUP_SUCCESS(true, 2200, "개인 회원 가입 성공"),
    PERSONAL_SIGNUP_NOT_FOUND_COMPANY(false, 2404, "회사 정보를 찾지 못했습니다."),
    SIGNUP_DUPLICATE_ID(false, 2403, "ID가 중복됩니다."),
    SIGNUP_DUPLICATE_EMAIL(false, 2403, "Email이 중복됩니다."),
    FIND_ID_SUCCESS(true, 2200, "ID 찾기 성공"),
    FIND_PW_SUCCESS(true, 2200, "비밀번호 찾기 성공"),
    RESET_PASSWORD_SUCCESS(true, 2200, "비밀번호 재설정 성공"),
    RESET_PASSWORD_UNMATCHED(false, 2403, "이전 비밀번호가 일치하지 않습니다."),
    RESET_PASSWORD_NULL(false, 2404, "uuid를 찾지 못했습니다."),
    MEMBER_DETAIL_SUCCESS(true, 2200, "회원 상세 정보 조회 성공"),
    MEMBER_MODIFY_SUCCESS(true, 2200, "회원 정보 수정 성공"),
    MEMBER_SEARCH_NOT_FOUND(false, 2404, "해당 회원을 찾지 못했습니다."),
    MEMBER_RESIGN_SUCCESS(true, 2200, "회원 탈퇴 처리에 성공했습니다."),
    MYINFO_RETRIEVE_SUCCESS(true, 2200, "내 정보 조회에 성공했습니다."),
    MYACTIVITY_RETRIEVE_SUCCESS(true, 2200, "내 활동내역 조회에 성공했습니다."),
    MEMBER_APPROVE_SUCCESS(true, 2200, "회원 승인 완료"),
    MEMBER_REJECT_SUCCESS(true, 2200, "회원 반려 완료"),
    MEMBER_LIST_SUCCESS(true, 2200, "회원 전체 리스트 반환"),

    // 협력사 기능(3000)
    PARTNER_LIST_SUCCESS(true, 3200, "협력사 리스트 반환 성공"),

    // 피드백 기능(4000)
    FEEDBACK_TEMPLATE_CREATE_SUCCESS(true, 4200, "피드백 양식 생성 성공"),
    FEEDBACK_TEMPLATE_RETRIEVE_SUCCESS(true, 4200, "피드백 양식 조회 성공"),
    FEEDBACK_TEMPLATE_NOT_FOUND(false, 4404, "피드백 존재하지 않음"),
    FEEDBACK_SUBMIT_SUCCESS(true, 4200, "피드백 작성 성공"),

    // 캘린더 관리(이벤트 & 캠페인) 6000
    CALENDAR_EVENT_REGISTER_SUCCESS(true,6200, "회사 일정 등록을 성공했습니다."),
    CALENDAR_EVENT_UPDATE_SUCCESS(true,6201, "회사 일정 수정을 성공했습니다."),
    CALENDAR_EVENT_DELETE_SUCCESS(true,6200, "회사 일정 삭제를 성공했습니다."),
    CALENDAR_LIST_SUCCESS(true,6200, "회사 일정 전체 리스트 반환"),
    CALENDAR_EVENT_BY_DAY_LIST_SUCCESS(true,6200, "회사 일정 일별 리스트 반환"),
    CALENDAR_EVENT_DETAIL_SUCCESS(true,6200, "회사 일정 상세 조회에 성공했습니다."),

    // 캘린더 - 캠페인 관리
    CALENDAR_CAMPAIGN_REGISTER_SUCCESS(true, 6400, "캠페인 참가 사원 등록을 성공했습니다."),
    CALENDAR_CAMPAIGN_UPDATE_SUCCESS(true, 6400, "캠페인 참가 사원 수정을 성공했습니다."),
    CALENDAR_CAMPAIGN_DETAIL_SUCCESS(true, 6400, "캠페인 상세 조회를 성공했습니다."),



    // 활동 관리 7000
    ADMIN_ACTIVITYLIST_FIND(true,7001,"관리자가 활동 리스트를 확인합니다."),
    USER_ACTIVITYLIST_FIND(true,7002,"개인 유저가 활동 리스트를 확인합니다."),
    ACTIVITY_APPROVAL_AGREE(true,7003,"활동이 승인 되었습니다."),
    ACTIVITY_APPROVAL_OPPOSE(true,7004,"활동이 반려 되었습니다."),
    MY_ACTIVITY_PROCESSED(false,7005,"이미 처리된 활동입니다."),
    ACTIVITY_APPROVAL_FALSE(false,7005,"활동 처리 권한이 없습니다."),
    ACTIVITY_REGIST_SUCCESS(true,7006,"활동 등록이 완료 되었습니다."),
    ACTIVITY_DELETE_SUCCESS(true,7007,"내 활동이 삭제 되었습니다."),
    ACTIVITY_DELETE_FALSE(false,7008,"삭제를 실패 하였습니다."),



    // 회사 점수 8000
    MY_COMPANY_SUCCESS(true, 8801, "회사 점수를 확인합니다."),




    SWGGER_SUCCESS(true,20000,"swagger 성공")


;

    private Boolean isSuccess;
    private Integer code;
    private String message;


}
