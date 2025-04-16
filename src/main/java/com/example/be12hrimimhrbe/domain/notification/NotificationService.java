package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import com.example.be12hrimimhrbe.domain.notification.model.NotificationDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void approveMsg(NotificationDto.ApproveMsgReq dto) {
        Member member= dto.getMember();

        member.setNotificationCount(member.getNotificationCount() + 1);
        memberRepository.save(member);

        Notification notification = Notification.builder()
                .isRead(false)
                .member(member)
                .content(dto.getContent())
                .title(dto.getTitle())
                .createdAt(LocalDateTime.now())
                .url(dto.getUrl())
                .build();
        notificationRepository.save(notification);

        simpMessagingTemplate.convertAndSend("/topic/notification/"+member.getIdx(), NotificationDto.NotificationResp.from(notification,member));
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<NotificationDto.NotificationResp>> getMyNotifications(Member member, int page, int size) {
        List<NotificationDto.NotificationResp> result = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
        Page<Notification> notis= notificationRepository.findAllByMember(member, pageable);

        for (Notification notification: notis) {
            result.add(NotificationDto.NotificationResp.from(notification,notification.getMember()));
        }

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);
    }
}
