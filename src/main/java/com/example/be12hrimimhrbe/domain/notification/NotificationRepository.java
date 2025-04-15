package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
