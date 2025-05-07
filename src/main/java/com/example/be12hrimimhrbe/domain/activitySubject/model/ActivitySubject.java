package com.example.be12hrimimhrbe.domain.activitySubject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "activitySubject")
public class ActivitySubject {
    @Id
    private String id;
    private Long companyIdx;
}
