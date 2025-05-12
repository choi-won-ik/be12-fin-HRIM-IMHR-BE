package com.example.be12hrimimhrbe.domain.rank.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

public class RankDto {

    @Getter @Builder
    public static class BatchRankResp{
        private Member member;
        private int average;
    }
}
