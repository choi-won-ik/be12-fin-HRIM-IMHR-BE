package com.example.batchapi.repository;

import com.example.batchapi.company.model.Company;
import com.example.batchapi.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.company c " +
            "LEFT JOIN FETCH m.department d " +
            "where c=:company")
    List<Member> findAllByCompany(Company company);
}
