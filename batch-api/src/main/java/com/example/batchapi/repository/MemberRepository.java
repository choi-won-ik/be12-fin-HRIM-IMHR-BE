package com.example.batchapi.repository;

import com.example.batchapi.entity.Company;
import com.example.batchapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByCompany(Company company);
}
