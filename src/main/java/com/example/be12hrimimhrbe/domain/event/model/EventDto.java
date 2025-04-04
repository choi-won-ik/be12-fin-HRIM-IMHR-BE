package com.example.be12hrimimhrbe.domain.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventDto {

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public class EventReistReq {
        private String eventTitle;
        private String eventDescription;
        private String eventTime;
    }
}
