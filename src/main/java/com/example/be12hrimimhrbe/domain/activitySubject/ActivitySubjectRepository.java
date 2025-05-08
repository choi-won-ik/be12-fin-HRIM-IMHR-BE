package com.example.be12hrimimhrbe.domain.activitySubject;

import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitySubjectRepository extends MongoRepository<ActivitySubject, String> {
    List<ActivitySubject> findByCompanyIdx(Long companyIdx);
}
