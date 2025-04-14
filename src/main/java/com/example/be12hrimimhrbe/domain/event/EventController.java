package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<BaseResponse<EventDto.EventResponse>> register(@AuthenticationPrincipal Company company, @RequestBody EventDto.EventRequest dto) {
        EventDto.EventResponse response = eventService.eventRegister(company, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseMessage.REQUEST_SUCCESS, response));
    }

    @GetMapping("/list")
    @Operation(summary = "일정 리스트", description = "이번달 일정을 확인 합니다.")
    public ResponseEntity<BaseResponse<Page<EventDto.EventResponse>>> list(
            @AuthenticationPrincipal Company company,
            @Parameter(hidden = true) Pageable pageable) {
        Page<EventDto.EventResponse> responses = eventService.eventList(company, pageable);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,responses));
    }

    @GetMapping("/date")
    @Operation(summary = "특정 날짜 일정 상세 조회", description = "선택 일정을 상세 조회 합니다.")
    public ResponseEntity<BaseResponse<List<EventDto.EventResponse>>> readEvent(
            @AuthenticationPrincipal Company company,
            @RequestParam("date") @Parameter(description = "조회할 날짜 (yyyy-MM-dd)") String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        List<EventDto.EventResponse> responses = eventService.readEventByDate(company, localDate);
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,responses));
    }

    @DeleteMapping("/delete/{idx}")
    @Operation(summary = "일정 제거", description = "선택 일정을 제거 합니다.")
    public ResponseEntity<BaseResponse<Boolean>> Delete(@PathVariable Long idx) {
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,null));
    }
}