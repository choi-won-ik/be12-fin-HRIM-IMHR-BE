package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.notification.model.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@Tag(name = "웹소켓 알람", description = "웹소켓 알람 컨트롤러")
public class NotificationController {
    private final NotificationService notificationService;

    @MessageMapping("/notification/{memberIdx}")
    @Operation(summary = "활동내역 승인", description = "활동내역 승인 알림 기능 입니다.")
    public void approveMsg(
            @Payload NotificationDto.ApproveMsgReq dto) {
        System.out.println(dto.getMember().getIdx());
        System.out.println(dto.getContent());
        notificationService.approveMsg(dto);

    }
}
