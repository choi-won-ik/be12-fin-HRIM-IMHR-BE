package com.example.be12hrimimhrbe.domain.campaign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class CampaignDto {

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CampaignListResponse {
        private List<CampaignItemResponse> campaigns;
    }

    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class CampaignItemResponse {
        private Long campaignIdx;
        private String content;
        private LocalDate date;
    }
}
