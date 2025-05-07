package com.example.batchapi.rank.model;

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
    }

//    @Getter @NoArgsConstructor @AllArgsConstructor @Builder
//    public static class MemberAverageeqsp{
//        private Member member;
//        private List<Integer> average;
//    }
}
