package com.example.be12hrimimhrbe.domain.partner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findAllByMainCompany_Idx(Long myCompanyIdx);

    Page<Partner> findAllByMainCompanyIdx(Long mainCompanyIdx, Pageable pageable);

    void deleteByPartnerCompanyIdxAndMainCompanyIdx(Long partnerCompanyIdx, Long mainCompanyIdx);

    Partner findByPartnerCompanyIdx(Long partnerCompanyIdx);
}