package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.campaign.model.CampaignDto;
import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.event.EventRepository;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Transactional
    public List<Long> register(CampaignDto.CampaignRequest req) {
        Event event = eventRepository.findById(req.getEventIdx()).orElseThrow();

        List<Member> members = memberRepository.findAllById(req.getMemberIdxList());
        List<Campaign> toSave = new ArrayList<>();
        List<Long> failed = new ArrayList<>();
        for (Member member : members) {
            try {
                boolean alreadyExists = campaignRepository.existsByEventAndMember(event, member);
                if (alreadyExists) continue;

                Campaign campaign = Campaign.builder()
                        .event(event)
                        .member(member)
                        .build();

                toSave.add(campaign);
            } catch (Exception e) {
                failed.add(member.getIdx());
            }
        }

        campaignRepository.saveAll(toSave);
        return failed;
    }
}
