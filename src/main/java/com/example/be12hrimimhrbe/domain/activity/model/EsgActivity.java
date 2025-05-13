package com.example.be12hrimimhrbe.domain.activity.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "esg_activity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsgActivity {
    @Id
    private String id;

    private Long memberIdx;
    private Long companyIdx;
    private String subject;

    private LocalDate activityDate;
    private String description;
    private Map<String, Object> inputs;
    private LocalDateTime createdAt;
}
