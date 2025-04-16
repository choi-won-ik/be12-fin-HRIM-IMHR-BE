package com.example.be12hrimimhrbe.domain.notification.model;

import com.example.be12hrimimhrbe.domain.member.model.Member;
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

    private String title;
    private String content;

    private String url;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private Member member;

    private LocalDateTime createdAt = LocalDateTime.now();
}