package com.example.be12hrimimhrbe.domain.notification.model;

import com.example.be12hrimimhrbe.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private Boolean isRead;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createdAt = LocalDateTime.now();
}