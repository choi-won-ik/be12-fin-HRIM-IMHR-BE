package com.example.be12hrimimhrbe.domain.partner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Long findMainCompanyIdxByPartnerIdx(Long partnerIdx);

    List<Partner> findAllByMainCompany_Idx(Long myCompanyIdx);

    Page<Partner> findAllByMainCompanyId(Long mainCompanyIdx, Pageable pageable);

    void deletePartnerAndMainCompanyByIdx(Long idx);
}