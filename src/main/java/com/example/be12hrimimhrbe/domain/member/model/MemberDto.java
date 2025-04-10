package com.example.be12hrimimhrbe.domain.member.model;

import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
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
        private Integer status;
        private String memberId;
        private String name;
        private LocalDateTime joinedAt;
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
        private Department department;
        private String role;
        private Integer status;
        private LocalDateTime joinedAt;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class InfoDetailResponse {
        private InfoResponse info;
        private DepartmentDto.DepartmentListResponse departments;
        private List<String> roles;
    }

    public static class InfoDetailRequest {
        private Long departmentIdx;
        private List<String> roles;
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
        public Member toMember(String encryptedPassword, Company company) {
            return Member.builder().memberId(this.memberId)
                    .name(this.name)
                    .email(this.email)
                    .isAdmin(false)
                    .hasPartnerAuth(false)
                    .hasProdAuth(false)
                    .status(Member.Status.APPROVED)
                    .company(company)
                    .code(employeeCode)
                    .password(encryptedPassword)
                    .joinedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PersonalSignupResponse {
        private Long member_idx;
        private String name;
        private String memberId;
        private String email;
        private String companyCode;
        private String employeeCode;
        public static PersonalSignupResponse fromMember(Member member) {
            return PersonalSignupResponse.builder()
                    .member_idx(member.getIdx())
                    .name(member.getName())
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .companyCode(member.getCompany().getCode())
                    .employeeCode(member.getCode())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanySignupRequest {
        private String name;
        private String memberId;
        private String password;
        private String email;
        private String companyName;
        private String registrationNumber;
        public Member toMember(String encryptedPassword, Company company) {
            return Member.builder().memberId(this.memberId)
                            .name(this.name)
                            .email(this.email)
                            .isAdmin(true)
                            .hasPartnerAuth(false)
                            .hasProdAuth(false)
                            .status(Member.Status.APPROVED)
                            .company(company)
                            .password(encryptedPassword)
                            .joinedAt(LocalDateTime.now())
                            .build();
        }
        public Company toCompany(String imgUrl) {
            return Company.builder()
                    .name(this.companyName)
                    .registrationNumber(registrationNumber)
                    .code(registrationNumber)
                    .imgUrl(imgUrl)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CompanySignupResponse {
        private Long member_idx;
        private String name;
        private String memberId;
        private String email;
        private String companyName;
        private String registrationNumber;
        public static CompanySignupResponse fromMember(Member member) {
            return CompanySignupResponse.builder()
                    .member_idx(member.getIdx())
                    .name(member.getName())
                    .email(member.getEmail())
                    .memberId(member.getMemberId())
                    .companyName(member.getCompany().getName())
                    .registrationNumber(member.getCompany().getRegistrationNumber())
                    .build();
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class LoginRequest {
        private String memberId;
        private String password;
        private String way;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportReq{
        private String startMonth;
        private String endMonth;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportDetailResp{
        private String memberName;
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
        private Long memberIdx;
        private String memberName;
        private String memberId;
        private String departmentName;
        private String startMonth;
        private String endMonth;
    }

    public static enum MemberReportUserFindType{
        NAME,ID,CODE
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class MemberReportUserFindResp {
        private Long memberIdx;
        private String memberName;
        private String memberId;
        private String departmentName;
        private String startMonth;
        private String endMonth;
    }

}
