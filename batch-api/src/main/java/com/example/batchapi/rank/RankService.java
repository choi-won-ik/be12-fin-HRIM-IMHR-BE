package com.example.batchapi.rank;


import com.example.batchapi.company.model.Company;
import com.example.batchapi.member.model.Member;
import com.example.batchapi.rank.model.Rank;
import com.example.batchapi.rank.model.RankDto;
import com.example.batchapi.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final MemberRepository memberRepository;


    public List<Rank> rankAggregater(Company item) {
        List<Rank> result = new ArrayList<>();
        List<Member> members = memberRepository.findAllByCompany(item);

        List<RankDto.Aggregateresp> list = new ArrayList<>();
        if (!members.isEmpty()) {
            for (Member member : members) {
                int average = (member.getEScore() + member.getSScore() + member.getGScore()) / 3;

                list.add(RankDto.Aggregateresp.builder()
                        .memberIdx(member.getIdx())
                        .average(average)
                        .member(member)
                        .build());
            }

            list.sort(Comparator.comparing(RankDto.Aggregateresp::getAverage).reversed());

            for (int i = 0; i < 3; i++) {

                int nowYear = LocalDateTime.now().getYear();
                int nowMonth = LocalDateTime.now().getMonthValue();

                // 1월이면 작년 12월로 보정
                if (nowMonth == 1) {
                    nowMonth = 12;
                    nowYear -= 1;
                } else {
                    nowMonth -= 1;
                }

                result.add(Rank.builder()
                        .member(list.get(i).getMember())
                        .company(item)
                        .average(list.get(i).getAverage())
                        .ranking(i + 1)
                        .year(nowYear)
                        .month(nowMonth)
                        .build());
            }
        }

        return result;
    }
}
