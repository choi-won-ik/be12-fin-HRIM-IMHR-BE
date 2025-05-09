package com.example.be12hrimimhrbe.domain.partner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import org.springframework.data.jpa.repository.Query;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    @Query(
            value = """
        SELECT p.*
        FROM partner p
        JOIN company pc ON p.partner_company_idx = pc.idx
        LEFT JOIN score s ON s.company_idx = pc.idx
            AND s.year = (
                SELECT MAX(s2.year)
                FROM score s2
                WHERE s2.company_idx = pc.idx
            )
        WHERE p.main_company_idx = :mainCompanyIdx
        ORDER BY
            CASE s.social_score
                WHEN 'A+' THEN 1
                WHEN 'B+' THEN 2
                WHEN 'B' THEN 3
                WHEN 'C+' THEN 4
                WHEN 'C' THEN 5
                WHEN 'D+' THEN 6
                WHEN 'D' THEN 7
                ELSE 8
              END ASC,
              ISNULL(s.social_score) ASC,
            CASE s.environment_score
                WHEN 'A+' THEN 1
                WHEN 'B+' THEN 2
                WHEN 'B' THEN 3
                WHEN 'C+' THEN 4
                WHEN 'C' THEN 5
                WHEN 'D+' THEN 6
                WHEN 'D' THEN 7
                ELSE 8
            END ASC,
              ISNULL(s.environment_score) ASC,
            CASE s.governance_score
                WHEN 'A+' THEN 1
                WHEN 'B+' THEN 2
                WHEN 'B' THEN 3
                WHEN 'C+' THEN 4
                WHEN 'C' THEN 5
                WHEN 'D+' THEN 6
                WHEN 'D' THEN 7
                ELSE 8
             END ASC,
              ISNULL(s.governance_score) ASC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM partner p
        WHERE p.main_company_idx = :mainCompanyIdx
        """,
            nativeQuery = true
    )
    Page<Partner> findAllByMainCompanyIdx(Long mainCompanyIdx, Pageable pageable);

    List<Partner> findAllByMainCompany_Idx(Long myCompanyIdx);

    void deleteByPartnerCompanyIdxAndMainCompanyIdx(Long partnerCompanyIdx, Long mainCompanyIdx);

    Partner findByPartnerCompanyIdx(Long partnerCompanyIdx);

    Page<Partner> findByPartnerCompanyNameContainingIgnoreCase(String partnerCompanyName, Pageable pageable);
}