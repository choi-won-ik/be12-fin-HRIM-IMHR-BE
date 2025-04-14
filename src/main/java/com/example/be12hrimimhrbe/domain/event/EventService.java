package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
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

    @Transactional
    public EventDto.EventResponse eventRegister(Company company, EventDto.EventRequest dto) {
        Event event = eventRepository.save(dto.toEntity(company));
        return EventDto.EventResponse.of(event);
    }

    public Page<EventDto.EventResponse> eventList(Company company, Pageable pageable) {
        if (company == null) {
            company = Company.builder().idx(1L).build(); // 임시 company idx
        }
        return eventRepository.findAllByCompanyIdx(company.getIdx(), pageable)
                .map(EventDto.EventResponse::of);
    }

    public EventDto.EventResponse readEventDetail(Company company, Long idx) {
        if (company == null) {
            company = Company.builder().idx(1L).build(); // 임시 company idx
        }
        Event event = eventRepository.findByIdxAndCompanyIdx(idx, company.getIdx()).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다."));
        return EventDto.EventResponse.of(event);
    }

    public List<EventDto.EventResponse> readEventByDate(Company company, LocalDate date) {
        if (company == null) {
            company = Company.builder().idx(1L).build(); // 임시 company idx
        }
        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual(company.getIdx(), date, date);
        return events.stream().map(EventDto.EventResponse::of).toList();
    }

    public boolean deleteEvent(Company company, Long idx) {
        if (company == null) {
            company = Company.builder().idx(1L).build(); // 임시 company idx
        }
        Event event = eventRepository.findByIdxAndCompanyIdx(idx, company.getIdx()).orElseThrow(() ->  new IllegalArgumentException("해당 일정이 존재하지 않거나 권한이 없습니다."));
        eventRepository.delete(event);
        return true;
    }
}