package com.example.be12hrimimhrbe.domain.event.model;

import com.example.be12hrimimhrbe.domain.company.model.Company;
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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private String content;
    private String color;
    private LocalDate startDate;
    private LocalDate endDate;

    public void updateFromDto(EventDto.EventRequest dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.color = dto.getColor();
    }

    @ManyToOne
    @JoinColumn(name = "company_idx")
    private Company company;
}