package com.example.be12hrimimhrbe.domain.campaign.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

public class CampaignDto {
    @Getter
    @Schema(description = "일정 등록 / 수정 요청 DTO")
    public static class CampaignRequest {
        @Schema(description = "캠페인 고유 실별 값", example = "1")
        private Long idx;

        @Schema(description = "이벤트(캠페인) idx", example = "1")
        private Long eventIdx;

        @Schema(description = "참여할 사원들의 idx 리스트", example = "[1, 2, 3]")
        private List<Long> memberIdxList;
    }
}
