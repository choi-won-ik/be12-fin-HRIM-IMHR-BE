package com.example.be12hrimimhrbe.domain.activity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivityDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActivityListRequest {
        private Long memberIdx;
    }

    @Getter
//    @Builder
    public static class ActivityListResponse {
        private List<ActivityItemResponse> activityList;

        public ActivityListResponse(List<Activity> list){
            for (Activity activity : list) {
                this.activityList.add(new ActivityItemResponse(activity));
            }
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActivityItemResponse {
        private Long activityIdx;
        private String title;
        private String startDate;

        private String memberId;
        private String memberName;

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
//        private String title;
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
