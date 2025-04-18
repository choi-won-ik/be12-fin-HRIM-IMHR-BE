package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.campaign.CampaignRepository;
import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final CampaignRepository campaignRepository;

//  이벤트 생성
    @Transactional
    public EventDto.EventResponse eventRegister(Member member, EventDto.EventRequest dto) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event = eventRepository.save(dto.toEntity(newMember.getCompany()));
        return EventDto.EventResponse.of(event);
    }

//  이벤트 수정
    public EventDto.EventResponse updateEvent(Member member, Long idx, EventDto.EventRequest dto) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event = eventRepository.findByCompanyIdxAndIdx(newMember.getCompany().getIdx(), idx);
        event.updateFromDto(dto);

        Event updated = eventRepository.save(event);
        return EventDto.EventResponse.of(updated);
    }

//  페이지별 이벤트 리스트
    public Page<EventDto.EventResponse> pageList(Member member, Pageable pageable) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Page<Event> events = eventRepository.findByCompanyIdx(newMember.getCompany().getIdx(), pageable);
        return events.map(EventDto.EventResponse::of);
    }

//  월별 이벤트 리스트
    public List<EventDto.EventResponse> eventList(Member member, int year, int month) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual
                (newMember.getCompany().getIdx(), end, start);
        return events.stream()
                .map(EventDto.EventResponse::of)
                .collect(Collectors.toList());
    }

//  일별 이벤트 리스트
    public List<EventDto.EventResponse> readEventByDate(Member member, LocalDate date) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual
                (newMember.getCompany().getIdx(), date, date);
        return events.stream().map(EventDto.EventResponse::of).toList();
    }

//  이벤트 상세 조회
    public EventDto.EventResponse readEventDetail(Member member, Long idx) {
        Member nm = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event = eventRepository.findByCompanyIdxAndIdx(nm.getCompany().getIdx(), idx);
        return EventDto.EventResponse.of(event);
    }

//  이벤트 삭제
    @Transactional
    public boolean deleteEvent(Member member, Long idx) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event = eventRepository.findByIdxAndCompanyIdx(idx, newMember.getCompany().getIdx()).orElseThrow();

        List<Campaign> campaigns = campaignRepository.findByEventIdx(idx);
        Set<Long> memberIdx = campaigns.stream().map(c -> c.getMember().getIdx()).collect(Collectors.toSet());
        if (!memberIdx.isEmpty()) {
            campaignRepository.deleteByEventIdxAndMember_IdxIn(idx, memberIdx);
        }

        campaignRepository.deleteByEventIdx(event.getIdx());
        eventRepository.deleteById(event.getIdx());
        return true;
    }
}