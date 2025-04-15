package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EventDto.EventResponse eventRegister(Member member, EventDto.EventRequest dto) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event = eventRepository.save(dto.toEntity(newMember.getCompany()));
        return EventDto.EventResponse.of(event);
    }

    public EventDto.EventResponse updateEvent(Member member, Long idx, EventDto.EventRequest dto) {
        Event event = eventRepository.findById(idx).orElseThrow();

        event.updateFromDto(dto);

        Event updated = eventRepository.save(event);
        return EventDto.EventResponse.of(updated);
    }

    public Page<EventDto.EventResponse> eventList(Long companyIdx, Pageable pageable) {
        return eventRepository.findAllByCompanyIdx(companyIdx, pageable)
                .map(EventDto.EventResponse::of);
    }

    public List<EventDto.EventResponse> readEventByDate(Long companyIdx, LocalDate date) {
        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual(companyIdx, date, date);
        return events.stream().map(EventDto.EventResponse::of).toList();
    }

    public EventDto.EventResponse readEventDetail(Company company, Long idx) {
        Event event = eventRepository.findByIdxAndCompanyIdx(idx, company.getIdx()).orElseThrow();
        return EventDto.EventResponse.of(event);
    }

    public boolean deleteEvent(Company company, Long idx) {
        Event event = eventRepository.findByIdxAndCompanyIdx(idx, company.getIdx()).orElseThrow(() ->  new IllegalArgumentException("해당 일정이 존재하지 않거나 권한이 없습니다."));
        eventRepository.delete(event);
        return true;
    }
}