package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EsgActivityRepository extends MongoRepository<EsgActivity, Long> {
    Page<EsgActivity> findByCompanyIdx(Long companyIdx, Pageable pageable);
}
