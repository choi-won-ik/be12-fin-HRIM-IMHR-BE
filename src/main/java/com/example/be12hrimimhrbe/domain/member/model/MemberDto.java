package com.example.be12hrimimhrbe.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FindIdRequest {
        public String name;
        public String email;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FindPWRequest {
        public String memberId;
        public String email;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ResetPasswordRequest {
        public String oldPassword;
        public String newPassword;
    }
}
