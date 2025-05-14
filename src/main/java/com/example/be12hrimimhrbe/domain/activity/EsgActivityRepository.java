package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivity;
import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EsgActivityRepository extends MongoRepository<EsgActivity, Long> {
    List<EsgActivity> findByCompanyIdx(Long companyIdx);

    Page<EsgActivity> findAllByCompanyIdx(Long companyIdx, Pageable pageable);
    Page<EsgActivity> findByCompanyIdxAndSubjectContainingIgnoreCase(Long companyIdx, String subject, Pageable pageable);

    Page<EsgActivity> findAllByMemberIdx(Long memberIdx, Pageable pageable);
    Page<EsgActivity> findByMemberIdxAndSubjectContainingIgnoreCase(Long memberIdx, String subject, Pageable pageable);
}
