package com.example.be12hrimimhrbe.domain.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventDto {

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class EventReistReq {
        private String eventTitle;
        private String eventDescription;
        private String eventTime;
    }

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class EventListReq {
        private String month;
    }

    @Getter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class EventListResp {
        private Long eventIdx;
        private String eventTitle;
        private String eventDescription;
        private String eventTime;
    }

}
