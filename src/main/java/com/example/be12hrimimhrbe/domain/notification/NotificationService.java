package com.example.be12hrimimhrbe.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void approveMsg(Long idx) {
        simpMessagingTemplate.convertAndSend("/topic/notification/approve/"+idx, notificationRepository.findAll());
    }
}
