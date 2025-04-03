package com.example.be12hrimimhrbe.domain.activity.model;

import com.example.be12hrimimhrbe.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String title;

    @Lob
    private String description;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDate performedAt;

    public enum Type { VOLUNTEER, DONATION, EDUCATION }
    public enum Status { PENDING, APPROVED, REJECTED }
}