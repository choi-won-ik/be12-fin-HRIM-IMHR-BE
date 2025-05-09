package com.example.be12hrimimhrbe.global.batch;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RankDto {

    @Data
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Aggregateresp{
        private Long memberIdx;
        private int average;
        private Member member;
    }

//    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
//    public static class MemberAverageeqsp{
//        private Member member;
//        private List<Integer> average;
//    }
}
