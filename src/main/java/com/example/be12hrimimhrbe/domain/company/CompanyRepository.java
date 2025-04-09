package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCode(String code);
}
