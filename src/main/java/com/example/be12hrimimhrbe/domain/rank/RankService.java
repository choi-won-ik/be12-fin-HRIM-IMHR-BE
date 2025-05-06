package com.example.be12hrimimhrbe.domain.rank;

import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.rank.model.Rank;
import com.example.be12hrimimhrbe.domain.rank.model.RankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final MemberRepository memberRepository;


    public Rank rankAggregater(Rank item) {
        List<Member> members = memberRepository.findAllByCompany(item.getCompany());


        RankDto.Aggregateresp dto = null;
        if(!members.isEmpty()) {
            for (Member member : members) {
                int average = (member.getEScore() + member.getSScore() + member.getGScore()) / 3;

                if (dto == null || dto.getAverage() < average) {
                    dto = RankDto.Aggregateresp.builder()
                            .memberIdx(member.getIdx())
                            .average(average)
                            .build();
                }
            }

            Optional<Member> topMember = memberRepository.findById(dto.getMemberIdx());

            if(!topMember.isEmpty()){
                memberUpdate(topMember.get());  // 내부에서 저장 처리
                item.setMember(topMember.get());
                item.setAverage(dto.getAverage());
            }
        }

        return item;
    }

    @Transactional
    public void memberUpdate(Member member) {
        boolean changed = false;

        if (member.getEScore() != 0) {
            member.setEScore(0);
            changed = true;
        }

        if (member.getSScore() != 0) {
            member.setSScore(0);
            changed = true;
        }

        if (member.getGScore() != 0) {
            member.setGScore(0);
            changed = true;
        }

        if (changed) {
            memberRepository.save(member);
        }
    }

}
