package com.example.be12hrimimhrbe.domain.partner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findAllByIdx(Long idx);
}