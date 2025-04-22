package com.example.be12hrimimhrbe.domain.score;

import com.example.be12hrimimhrbe.domain.score.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Collection<Score> findByCompanyIdx(Long partnerIdx);

}
