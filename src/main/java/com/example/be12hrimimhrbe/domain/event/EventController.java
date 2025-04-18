package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/event")
@Tag(name = "이벤트 관리 기능")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/register")
    @Operation(summary = "일정 등록", description = "새 일정을 등록하는 기능 입니다.")
    public ResponseEntity<BaseResponse<EventDto.EventResponse>> register(@AuthenticationPrincipal CustomUserDetails member, @RequestBody EventDto.EventRequest dto) {
        EventDto.EventResponse response = eventService.eventRegister(member.getMember(), dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_REGISTER_SUCCESS, response));
    }

    @PutMapping("/update/{eventIdx}")
    @Operation(summary = "일정 수정", description = "등록했던 이벤트를 수정하는 기능입니다.")
    public ResponseEntity<BaseResponse<EventDto.EventResponse>> update(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long eventIdx,
            @RequestBody EventDto.EventRequest dto
    ) {
        EventDto.EventResponse response = eventService.updateEvent(member.getMember(), eventIdx, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_UPDATE_SUCCESS, response));
    }

    //  회사 페이지별 일정 리스트 - 캠페인 페이지
    @GetMapping("/pageList")
    @Operation(summary = "기업 페이지별 일정 리스트", description = "페이지별로 일정을 조회 합니다.")
    public ResponseEntity<BaseResponse<Page<EventDto.EventResponse>>> pageList(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventDto.EventResponse> responses = eventService.pageList(member.getMember(), pageable);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_LIST_SUCCESS, responses));
    }

    // 월별 일정 리스트
    @GetMapping("/month/list")
    @Operation(summary = "기업 월별 일정 리스트", description = "월별 일정을 확인합니다.")
    public ResponseEntity<BaseResponse<List<EventDto.EventResponse>>> list(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam int year,
            @RequestParam int month
    )
    {
        List<EventDto.EventResponse> responses = eventService.eventList(member.getMember(), year, month);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_LIST_SUCCESS, responses));
    }

//  일별 일정 리스트 조회
    @GetMapping("/date/list")
    @Operation(summary = "일별 일정 리스트 조회", description = "선택 날짜의 일정 리스트를 조회 합니다.")
    public ResponseEntity<BaseResponse<List<EventDto.EventResponse>>> readEvent(
            @AuthenticationPrincipal CustomUserDetails member,
            @RequestParam("date") @Parameter(description = "조회할 날짜 (yyyy-MM-dd)") String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        List<EventDto.EventResponse> responses = eventService.readEventByDate(member.getMember(), localDate);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_BY_DAY_LIST_SUCCESS, responses));
    }

    @GetMapping("/eventDetail/{idx}")
    @Operation(summary = "특정 일정 상세 조회", description = "선택 일정을 상세 조회 합니다.")
    public ResponseEntity<BaseResponse<EventDto.EventResponse>> readEventDetail(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long idx
    ) {
        EventDto.EventResponse response = eventService.readEventDetail(member.getMember(), idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_DETAIL_SUCCESS, response));
    }

    @DeleteMapping("/delete/{idx}")
    @Operation(summary = "일정 제거", description = "선택 일정과 캠페인 그리고 캠페인에 참가한 사원 내역을 제거하는 기능입니다.")
    public ResponseEntity<BaseResponse<Boolean>> Delete(
            @AuthenticationPrincipal CustomUserDetails member,
            @PathVariable Long idx) {
        boolean isDeleted = eventService.deleteEvent(member.getMember(), idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.CALENDAR_EVENT_DELETE_SUCCESS, isDeleted));
    }
}