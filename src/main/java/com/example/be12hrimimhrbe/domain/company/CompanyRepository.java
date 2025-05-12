package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCode(String code);
    Optional<Company> findByName(String name);

    Company findByIdx(Long idx);

    @Query("SELECT c FROM Company c WHERE c.idx NOT IN :excludedIds")
    Page<Company> findAllExcludingIds(
            @Param("excludedIds") List<Long> excludedIds,
            Pageable pageable
    );

    Page<Company> findByNameContainingIgnoreCaseAndIdxNotIn(String name, List<Long> idxes, Pageable pageable);

    @EntityGraph(attributePaths = {"ranks", "ranks.member"})
    @Query("SELECT c FROM Company c " +
            "LEFT JOIN c.ranks r " +
            "LEFT JOIN r.member m " +
            "WHERE c.idx=:idx")
    Company findByIdxRank(Long idx);
}