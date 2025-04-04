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
        private Long idx;
        private Integer type;
        private String memberId;
        private String name;
        private LocalDate joinedAt;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FindIdRequest {
        private String name;
        private String email;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class FindPWRequest {
        private String memberId;
        private String email;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ResetPasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class InfoResponse {
        private Long idx;
        private String name;
        private String email;
        private String company;
        private String department;
        private String role;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityResponse {
        private Long member_idx;
        private List<ActivityItem> activities;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityItem {
        private Long activityIdx;
        private Long campaignIdx;
        private String type;
        private String content;
        private LocalDate date;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PersonalSignupRequest {
        private String name;
        private String memberId;
        private String email;
        private String password;
        private String companyCode;
        private String employeeCode;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PersonalSignupResponse {
        private Long member_idx;
        private String name;
        private String memberId;
        private String email;
        private String companyCode;
        private String employeeCode;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanySignupRequest {
        private String name;
        private String memberId;
        private String password;
        private String email;
        private String companyName;
        private String registrationNumber;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanySignupResponse {
        private Long member_idx;
        private String name;
        private String memberId;
        private String email;
        private String companyName;
        private String registrationNumber;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportReq{
        private String startMonth;
        private String endMonth;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportDetailResp{
        private String userName;
        private String departmentName;
        private int individualScore;
        private List<String> feedbackResponseAnswerText;
        private List<ActivityDto.ActivityReportDetailResp> activityList;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportUserFindReq {
        private MemberReportUserFindType type;
        private String findContent;
        private String startMonth;
        private String endMonth;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportListResp{
        private Long userIdx;
        private String userName;
        private String userId;
        private String departmentName;
        private String startMonth;
        private String endMonth;
    }

    public static enum MemberReportUserFindType{
        NAME,ID,CODE
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportUserFindResp {
        private Long userIdx;
        private String userName;
        private String userId;
        private String departmentName;
        private String startMonth;
        private String endMonth;
    }


}
