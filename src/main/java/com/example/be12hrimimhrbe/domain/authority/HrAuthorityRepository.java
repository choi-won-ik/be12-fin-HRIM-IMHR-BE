package com.example.be12hrimimhrbe.domain.authority;

import com.example.be12hrimimhrbe.domain.authority.model.HrAuthority;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HrAuthorityRepository extends JpaRepository<HrAuthority, Long> {
    List<HrAuthority> findAllByMember(Member member);
}
