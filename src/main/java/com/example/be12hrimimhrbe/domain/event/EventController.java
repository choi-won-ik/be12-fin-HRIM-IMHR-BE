package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.event.model.EventDto;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@Tag(name = "이벤트 관리 기능")
public class EventController {
    @PostMapping("/regist")
    @Operation(summary = "일정 등록", description = "새 일정을 등록하는 기능 입니다.")
    public ResponseEntity<BaseResponse<Long>> Regist(@RequestBody EventDto.EventReistReq Dto) {
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS, new Event().getIdx()));
    }

    @GetMapping("/list")
    @Operation(summary = "일정 리스트", description = "이번달 일정을 확인 합니다.")
    public ResponseEntity<BaseResponse<List<EventDto.EventListResp>>> List(@RequestBody EventDto.EventListReq Dto) {
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,null));
    }

    @DeleteMapping("/delete/{idx}")
    @Operation(summary = "일정 제거", description = "선택 일정을 제거 합니다.")
    public ResponseEntity<BaseResponse<Boolean>> Delete(@PathVariable Long idx) {
        return ResponseEntity.ok(new BaseResponse(BaseResponseMessage.SWGGER_SUCCESS,null));
    }
}
