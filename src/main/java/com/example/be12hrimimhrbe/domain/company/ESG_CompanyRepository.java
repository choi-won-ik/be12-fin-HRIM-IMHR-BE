package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.company.model.ESGCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ESG_CompanyRepository extends JpaRepository<ESGCompany, Long> {
    ESGCompany findByIdx(Long idx);
}
