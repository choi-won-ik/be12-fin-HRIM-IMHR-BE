package com.example.be12hrimimhrbe.domain.activity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ActivityDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityListRequest {
        private Long memberIdx;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityListResponse {
        private List<ActivityItemResponse> activityList;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityItemResponse {
        private Long activityIdx;
        private Integer status;
        private String type;
        private String content;
        private String fileUrl;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityReportDetailResp{
        private String title;
        private String performedAt;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ActivityRegistReq{
        private String userId;
        private Activity.Type type;
        private String title;
        private String description;
        private String fileUrl;
    }
}
