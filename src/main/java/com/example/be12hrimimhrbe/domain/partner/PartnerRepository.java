package com.example.be12hrimimhrbe.domain.partner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findAllByIdx(Long idx);

    boolean existsByMainCompanyIdxAndPartnerIdx(Long mainCompanyIdx, Long partnerIdx);

    Long findMainCompanyIdxByPartnerIdx(Long partnerIdx);

    List<Long> findAllByMainCompanyIdx(Long myCompanyIdx);

    List<Partner> findAllByMainCompany_Idx(Long myCompanyIdx);
}