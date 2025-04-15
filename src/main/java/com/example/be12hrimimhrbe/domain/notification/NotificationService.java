package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.notification.model.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void approveMsg(NotificationDto.ApproveMsgReq dto) {
        System.out.println("서비스 실행");
        Member member= dto.getMember();

        member.setNotificationCount(member.getNotificationCount() + 1);
        System.out.println(member.getIdx());
        memberRepository.save(member);
        simpMessagingTemplate.convertAndSend("/topic/notification/"+member.getIdx(), "test");
    }
}
