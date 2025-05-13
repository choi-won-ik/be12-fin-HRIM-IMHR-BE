package com.example.be12hrimimhrbe.domain.activitySubject.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activitySubject")
public class ActivitySubject {
    @Id
    private String id;

    private Long companyIdx;
    private Long memberIdx;
    private String subject;
    private String esgValue;
    private int esgScore;
    private String esgActivityItem;
    private String evaluationCriteria;
    private List<input> inputs;


    @Getter @Builder @NoArgsConstructor @AllArgsConstructor @Setter
    public static class input {
        private String text;
        private String type;
        private String inputValue;
    }
}
