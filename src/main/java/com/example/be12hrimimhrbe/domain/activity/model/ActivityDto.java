package com.example.be12hrimimhrbe.domain.activity.model;

import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivityDto {

    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PageActivityListResp{
        private List<ActivityListResp> activityList;
        private int total;
    }

    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityListResp{
        private Long activityIdx;
        private Long memberIdx;
        private String title;
        private String startDate;
        private String memberId;
        private String memberName;

        private String status;
        private String type;
        private String description;
        public static ActivityListResp to(Activity activity, Member member) {
            String formattedDate = activity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return ActivityListResp.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getName())
                    .memberIdx(member.getIdx())
                    .activityIdx(activity.getIdx())
                    .startDate(formattedDate)
                    .title(activity.getTitle())
                    .description(activity.getDescription())
                    .build();
        }

        public static ActivityListResp findType(Activity activity,ActivityListResp index) {
            if(activity.getType()== Activity.Type.VOLUNTEER){
                index.setType("봉사");
            } else if (activity.getType()==Activity.Type.DONATION) {
                index.setType("기부");
            }
            return index;
        }

        public static ActivityListResp findStatus(Activity activity, ActivityListResp index) {
            if(activity.getStatus() == Activity.Status.PENDING) {
                index.setStatus("대기 중");
            } else if (activity.getStatus() == Activity.Status.APPROVED) {
                index.setStatus("승인");
            } else if (activity.getStatus()==Activity.Status.REJECTED) {
                index.setStatus("승인 반려");
            }

            return index;
        }
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityItemResponse {
        private Long activityIdx;
        private String title;
        private String startDate;

        private ActivityMember member;

        private Activity.Status status;
        private Activity.Type type;
        private String content;
        private String fileUrl;
        public ActivityItemResponse(Activity activity) {
            this.activityIdx=activity.getIdx();
            this.title = activity.getTitle();
            this.startDate = activity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActivityMember{
        private Long idx;
        private String name;
        private String memberId;
        private Boolean isAdmin;
        private Boolean hasProdAuth;
        private Boolean hasPartnerAuth;
        private String code;
        public ActivityMember(Member member) {
            this.idx=member.getIdx();
            this.name=member.getName();
            this.memberId=member.getMemberId();
            this.isAdmin=member.getIsAdmin();
            this.hasProdAuth=member.getHasProdAuth();
            this.hasPartnerAuth=member.getHasPartnerAuth();
            this.code=member.getCode();
        }
    }



    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActivityReportDetailResp {
        private String title;
        private String performedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActivityRegistReq {
        //        private String userId;
        private String type;
        private String title;
        private String description;
//        private String educationType;
//        private Activity.EducationType educationType;
        private int performance;

        public static Activity toEntity(Member member,ActivityRegistReq dto,Activity.Type activityType,String uploadFilePath) {
            return Activity.builder()
                    .member(member)
                    .type(activityType)
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .fileUrl(uploadFilePath)
                    .donation(dto.getPerformance())
                    .createdAt(LocalDateTime.now())
                    .status(Activity.Status.PENDING)
                    .build();
        }

        public static Activity toEntityEdu(Member member,ActivityRegistReq dto,Activity.Type activityType,String uploadFilePath,Activity.EducationType educationType) {
            return Activity.builder()
                    .member(member)
                    .type(activityType)
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .fileUrl(uploadFilePath)
                    .donation(dto.getPerformance())
                    .createdAt(LocalDateTime.now())
                    .status(Activity.Status.PENDING)
                    .educationType(educationType)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ativityApprovalResp {
        private Activity.Type type;
        private String title;
        private String userName;
        private String userId;
        private String fileUrl;
        private String description;

    }
}
