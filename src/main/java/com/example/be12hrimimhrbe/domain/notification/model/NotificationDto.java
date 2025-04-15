package com.example.be12hrimimhrbe.domain.notification.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationDto {
    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApproveMsgReq {
        private Member member;
        private String title;
        private String content;
    }
}
