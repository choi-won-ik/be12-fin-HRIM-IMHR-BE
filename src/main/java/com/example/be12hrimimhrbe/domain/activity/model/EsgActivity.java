package com.example.be12hrimimhrbe.domain.activity.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String userName;
    private String userID;
    private Long companyIdx;
    private String subject;
    private String segScore;
    private Boolean status;

    private LocalDate activityDate;
    private Map<String, Object> inputs;
    private LocalDateTime createdAt;
}
