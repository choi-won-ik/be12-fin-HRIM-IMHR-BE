package com.example.be12hrimimhrbe.domain.score;

import com.example.be12hrimimhrbe.domain.score.model.Score;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Collection<Score> findByCompanyIdx(Long partnerIdx);

    @EntityGraph(attributePaths = {"company","company.members"})
    @Query("SELECT s from Score s " +
            "LEFT JOIN s.company c " +
            "LEFT JOIN c.members m " +
            "where m.idx=:idx")
    List<Score> findByMemberIdx(Long idx);
}
