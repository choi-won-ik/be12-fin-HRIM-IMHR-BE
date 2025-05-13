package com.example.be12hrimimhrbe.domain.rank;

import com.example.be12hrimimhrbe.domain.rank.model.Rank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {

    @EntityGraph(attributePaths = {"company","member","member.company"})
    @Query("SELECT r FROM Rank r " +
            "LEFT JOIN r.company c " +
            "LEFT JOIN r.member m " +
            "LEFT JOIN m.company c2 " +
            "where c.idx= :idx " +
            "AND r.year=:year " +
            "AND r.month=:month ")
    List<Rank> findByCompanyIdx(Long idx,int year, int month);
}
