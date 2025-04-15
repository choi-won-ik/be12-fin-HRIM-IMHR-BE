package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "웹소켓 알람", description = "웹소켓 알람 컨트롤러")
public class NotificationController {
    private final NotificationService notificationService;

    @MessageMapping("/notification/{memberIdx}")
    @Operation(summary = "활동내역 승인", description = "활동내역 승인 알림 기능 입니다.")
    public void approveMsg(@PathVariable Long memberIdx, @AuthenticationPrincipal CustomUserDetails member) {
        if(member.getMember().getIdx()==memberIdx)
            notificationService.approveMsg(memberIdx);
    }
}
