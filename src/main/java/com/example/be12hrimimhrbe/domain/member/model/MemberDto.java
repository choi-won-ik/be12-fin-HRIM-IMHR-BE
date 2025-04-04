package com.example.be12hrimimhrbe.domain.member.model;

import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberShortResponse {
        public Long idx;
        public String memberId;
        public String name;
    }

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

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class InfoResponse {
        public Long idx;
        public String name;
        public String email;
        public String company;
        public String department;
        public String role;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityResponse {
        public Long member_idx;
        public List<ActivityItem> activities;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityItem {
        public Long activityIdx;
        public Long campaignIdx;
        public String type;
        public String content;
        public LocalDate date;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PersonalSignupRequest {
        public String name;
        public String memberId;
        public String email;
        public String password;
        public String companyCode;
        public String employeeCode;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PersonalSignupResponse {
        public Long member_idx;
        public String name;
        public String memberId;
        public String email;
        public String companyCode;
        public String employeeCode;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ReportDetailResp{
        private String userName;
        private String departmentName;
        private int individualScore;
        private List<String> feedbackResponseAnswerText;
        private List<ActivityDto.ActivityReportDetailResp> activityList;
    }
}
