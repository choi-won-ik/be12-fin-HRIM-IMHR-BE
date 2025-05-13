package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivity;
import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EsgActivityRepository extends MongoRepository<EsgActivity, Long> {
//    Page<EsgActivity> findByCompanyIdx(Long companyIdx, Pageable pageable);

    List<EsgActivity> findByMemberIdx(Long memberIdx);
    List<EsgActivity> findByCompanyIdx(Long companyIdx);
}
