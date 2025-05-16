package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.campaign.CampaignRepository;
import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
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
    public BaseResponse<EventDto.EventResponse> eventRegister(Member member, EventDto.EventRequest dto) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();
        Event event;

        if (dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("끝나는 날짜는 시작 날짜보다 빠를 수 없습니다.");
        } else if (!newMember.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }else  {
            event = eventRepository.save(dto.toEntity(newMember.getCompany()));
        }
        return new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_REGISTER_SUCCESS, EventDto.EventResponse.of(event));
    }

//  이벤트 수정
    public BaseResponse<EventDto.EventResponse> updateEvent(Member member, Long idx, EventDto.EventRequest dto) {
        Member newMember = memberRepository.findById(member.getIdx()).orElseThrow();

        if (!newMember.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }
        Event event = eventRepository.findByCompanyIdxAndIdx(newMember.getCompany().getIdx(), idx);
        event.updateFromDto(dto);
        Event updated = eventRepository.save(event);
        return new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_UPDATE_SUCCESS, EventDto.EventResponse.of(updated));
    }

//  페이지별 이벤트 리스
    public BaseResponse<Page<EventDto.EventResponse>> pageList(Member member, Pageable pageable, String keyword) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        Page<Event> events;

        if (keyword != null && !keyword.isBlank() ) {
            events = eventRepository.findByTitleContainingIgnoreCaseAndCompanyIdx(keyword, myCompanyIdx, pageable);
        } else {
            events = eventRepository.findByCompanyIdx(myCompanyIdx, pageable);
        }

        return new BaseResponse<>(BaseResponseMessage.CALENDAR_LIST_SUCCESS, events.map(EventDto.EventResponse::of));
    }

//  월별 이벤트 리스트
    public BaseResponse<List<EventDto.EventResponse>> eventList(Member member, int year, int month) {
        Long myCompanyIdx = member.getCompany().getIdx();
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual
                (myCompanyIdx, end, start);
        return new BaseResponse<>(BaseResponseMessage.CALENDAR_LIST_SUCCESS, events.stream()
                .map(EventDto.EventResponse::of)
                .collect(Collectors.toList()));
    }

//  일별 이벤트 리스트
    public BaseResponse<List<EventDto.EventResponse>> readEventByDate(Member member, LocalDate date) {
        Long myCompanyIdx = member.getCompany().getIdx();
        List<Event> events = eventRepository.findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual
                (myCompanyIdx, date, date);
        return new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_BY_DAY_LIST_SUCCESS, events.stream().map(EventDto.EventResponse::of).toList());
    }

//  이벤트 상세 조회
    public BaseResponse<EventDto.EventResponse> readEventDetail(Member member, Long idx) {
        Long myCompanyIdx = member.getCompany().getIdx();
        Event event = eventRepository.findByCompanyIdxAndIdx(myCompanyIdx, idx);
        return new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_DETAIL_SUCCESS, EventDto.EventResponse.of(event));
    }

//  이벤트 삭제
    @Transactional
    public boolean deleteEvent(Member member, Long idx) {
        Member newMember = memberRepository.findByIdx(member.getIdx());
        Long myCompanyIdx = member.getCompany().getIdx();
        Long eventIdx = eventRepository.findByIdxAndCompanyIdx(idx, myCompanyIdx).getIdx();

        if (!newMember.getIsAdmin()) {
            return false;
        }
        
        List<Campaign> campaigns = campaignRepository.findByEventIdx(idx);
        Set<Long> memberIdx = campaigns.stream().map(c -> c.getMember().getIdx()).collect(Collectors.toSet());
        if (!memberIdx.isEmpty()) {
            campaignRepository.deleteByEventIdxAndMember_IdxIn(idx, memberIdx);
        }

        campaignRepository.deleteByEventIdx(eventIdx);
        eventRepository.deleteById(eventIdx);
        return true;
    }
}