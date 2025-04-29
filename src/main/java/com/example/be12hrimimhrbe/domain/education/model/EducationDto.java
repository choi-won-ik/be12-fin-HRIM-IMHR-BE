package com.example.be12hrimimhrbe.domain.education.model;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EducationDto {
//    @Data
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class EducationListReq {
//
//    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageEducationListResp {
        private List<EducationListResp> educationList;
        private int total;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EducationListResp {
        private Long activityIdx;
        private Long memberIdx;
        private String title;
        private String startDate;
        private String memberId;
        private String memberName;

        private String status;
        private String type;
        private String description;

        public static EducationListResp to(Activity activity, Member member) {
            String formattedDate = activity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return EducationListResp.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getName())
                    .memberIdx(member.getIdx())
                    .activityIdx(activity.getIdx())
                    .startDate(formattedDate)
                    .title(activity.getTitle())
                    .description(activity.getDescription())
                    .build();
        }

        public static EducationListResp findType(Activity activity, EducationListResp index) {
            if(activity.getEducationType().equals(Activity.EducationType.ENVIRONMENTAL_EDUCATION))
                index.setType("환경");
            else if(activity.getEducationType().equals(Activity.EducationType.SOCIAL_EDUCATION))
                index.setType("사회");
            else if(activity.getEducationType().equals(Activity.EducationType.GOVERNANCE_EDUCATION))
                index.setType("지배구조");

            return index;
        }

        public static EducationListResp findStatus(Activity activity, EducationListResp index) {
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
}
