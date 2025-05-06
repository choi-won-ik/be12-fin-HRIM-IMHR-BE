package com.example.be12hrimimhrbe.domain.rank.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.*;

import java.util.List;

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
