package com.example.be12hrimimhrbe.domain.activity.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityDto {

    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityListResp{
        private Long activityIdx;
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
            } else if(activity.getType()==Activity.Type.EDUCATION){
                index.setType("교육");
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

//        private String memberId;
//        private String memberName;
//        private String memberRole;
        private Member member;

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
        //        private String startDate;
        private int performance;
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
