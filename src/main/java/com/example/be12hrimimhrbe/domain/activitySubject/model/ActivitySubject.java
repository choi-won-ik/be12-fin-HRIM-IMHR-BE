package com.example.be12hrimimhrbe.domain.activitySubject.model;

import jakarta.persistence.OneToMany;
import lombok.*;
import org.aspectj.weaver.ast.Literal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activitySubject")
public class ActivitySubject {
    @Id
    private String id;
    private Long companyIdx;
    private String subject;
    private List<input> inputs;


    @Getter @Builder @NoArgsConstructor @AllArgsConstructor @Setter
    public static class input {
        private String text;
        private String type;
    }
}
