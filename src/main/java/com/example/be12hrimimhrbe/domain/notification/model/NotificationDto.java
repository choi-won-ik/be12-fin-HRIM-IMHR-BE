package com.example.be12hrimimhrbe.domain.notification.model;

import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationDto {
    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ActivityApproveReq {
        private ActivityDto.ActivityMember member;
        private String title;
        private String content;
        public String url;
    }

    @Builder @Getter
    public static class MemberDto{
        private Long idx;
        private String name;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class NotificationResp {
        private Long idx;
        private MemberDto member;
        private String title;
        private String content;
        private boolean isRead;
        private String createdAt;
        private String url;

        public static NotificationResp from(Notification notification, Member member) {
            String formattedDate = notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            MemberDto dto = MemberDto.builder()
                    .idx(member.getIdx())
                    .name(member.getName())
                    .build();
            return NotificationResp.builder()
                    .idx(notification.getIdx())
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .createdAt(formattedDate)
                    .isRead(notification.getIsRead())
                    .member(dto)
                    .url(notification.getUrl())
                    .build();
        }
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SignupApproveReq {
        private String title;
        private String content;
        private String url;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ActivityApproveRequest{
        private String title;
        private String content;
        private String url;
        private Long activityIdx;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class eventcampaignMemberAddReq{
        private String title;
        private String content;
        private List<Long> memberIdx;
    }

    public static Notification isReadEntity(Member member,Notification notification) {
        return Notification.builder()
                .url(notification.getUrl())
                .isRead(true)
                .title(notification.getTitle())
                .content(notification.getContent())
                .member(member)
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
