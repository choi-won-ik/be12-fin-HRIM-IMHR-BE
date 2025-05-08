package com.example.batchapi.rank;


import com.example.batchapi.company.model.Company;
import com.example.batchapi.member.model.Member;
import com.example.batchapi.rank.model.Rank;
import com.example.batchapi.rank.model.RankDto;
import com.example.batchapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankService {
    private final MemberRepository memberRepository;


    public List<Rank> rankAggregater(Company item) {
        List<Rank> result = new ArrayList<>();
        List<Member> members = memberRepository.findAllByCompany(item);

        List<RankDto.Aggregateresp> list = new ArrayList<>();
        if(!members.isEmpty()) {
            for (Member member : members) {
                int average = (member.getEScore() + member.getSScore() + member.getGScore()) / 3;

                list.add(RankDto.Aggregateresp.builder()
                        .memberIdx(member.getIdx())
                        .average(average)
                        .member(member)
                        .build());
            }

            list.sort(Comparator.comparing(RankDto.Aggregateresp::getAverage).reversed());

            for(int i = 0; i<3; i++){
                System.out.println(list.get(i).getAverage()+" "+list.get(i).getMember().getIdx());


                result.add(Rank.builder()
                        .member(list.get(i).getMember())
                        .company(item)
                        .average(list.get(i).getAverage())
                        .ranking(i+1)
                        .year(LocalDateTime.now().getYear())
                        .month(LocalDateTime.now().getMonthValue())
                        .build());
            }
        }

        return result;
    }
}
