package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 테스트용: 인증 주체가 없을 때 임시 company 설정 (테스트 중에만 사용하세요)
        if (company == null) {
            company = Company.builder().idx(1L).build(); // 임시 company idx
        }
        return eventRepository.findAllByCompanyIdx(company.getIdx(), pageable)
                .map(EventDto.EventResponse::of);
    }
}
