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
    INTERNAL_SERVER_ERROR(false, 500, "내부 서버 오류"),

    // ========================================================================================================================
    // 회원 기능(2000)
    // 회원가입 2000
    LOGIN_SUCCESS(true, 2201, "로그인 되었습니다."),
    FIND_ID_SUCCESS(true, 2200, "ID 찾기 성공"),














;

    private Boolean isSuccess;
    private Integer code;
    private String message;


}
