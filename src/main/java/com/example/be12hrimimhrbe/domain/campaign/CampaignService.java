package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.campaign.model.CampaignDto;
import com.example.be12hrimimhrbe.domain.event.EventRepository;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
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


    @Transactional
    public List<MemberDto.MemberShortResponse> memberList(Long eventIdx) {
        List<Campaign> memberList = campaignRepository.findByEventIdx(eventIdx);
        List<Long> memberIds = memberList.stream()
                .map(c -> c.getMember().getIdx())
                .collect(Collectors.toList());

        List<Member> filteredMembers = memberRepository.findAllByIdxInAndIsAdminFalse(memberIds);

        return filteredMembers.stream()
                .map(MemberDto.MemberShortResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MemberDto.MemberShortResponse> update(Long eventIdx, CampaignDto.CampaignRequest dto) {
        // 원래 캠페인 정보
        Event event = eventRepository.findById(dto.getEventIdx()).orElseThrow();
        List<Campaign> campaign = campaignRepository.findByEventIdx(eventIdx);

        // 기존 참여자 목록 조회
        Set<Long> memberIds = campaign.stream()
                .map(c -> c.getMember().getIdx())
                .collect(Collectors.toSet());

        // update 할 멤버 idx 목록
        Set<Long> newMemberIdxs = new HashSet<>(dto.getMemberIdxList());

        // 추가 (new - old)
        Set<Long> toAdd = new HashSet<>(newMemberIdxs);
        toAdd.removeAll(memberIds);

        // 제거 (old - new)
        Set<Long> toRemove = new HashSet<>(memberIds);
        toRemove.removeAll(newMemberIdxs);

        // 삭제
        if (!toRemove.isEmpty()) {
            campaignRepository.deleteByEventIdxAndMember_IdxIn(eventIdx, toRemove);
        }

        // 추가
        List<Member> membersToAdd = memberRepository.findAllById(toAdd);
        List<Campaign> toSave = membersToAdd.stream()
                .map(member -> Campaign.builder()
                        .event(event)
                        .member(member)
                        .build())
                .collect(Collectors.toList());
        campaignRepository.saveAll(toSave);

        return membersToAdd.stream().map(MemberDto.MemberShortResponse::fromEntity)
                .collect(Collectors.toList());
    }
}