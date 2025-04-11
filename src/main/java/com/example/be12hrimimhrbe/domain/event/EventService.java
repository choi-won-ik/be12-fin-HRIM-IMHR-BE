package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {
    private EventRepository eventRepository;

    @Transactional
    public BaseResponse<EventDto.EventResponse> eventRegister(Company company, EventDto.EventRequest dto) {
        Event event = eventRepository.save(dto.toEntity(company));
        EventDto.EventResponse response = EventDto.EventResponse.of(event);
    }
}
