package com.example.be12hrimimhrbe.domain.notification.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

public class NotificationDto {
    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ApproveMsgReq {
        private Member member;
        private String title;
        private String content;
        public String url;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class NotificationResp {
        private Member member;
        private String title;
        private String content;
        private boolean isRead;
        private String createdAt;
        private String url;

        public static NotificationResp from(Notification notification, Member member) {
            String formattedDate = notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return NotificationResp.builder()
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .createdAt(formattedDate)
                    .isRead(notification.getIsRead())
                    .member(member)
                    .url(notification.getUrl())
                    .build();
        }
    }
}
