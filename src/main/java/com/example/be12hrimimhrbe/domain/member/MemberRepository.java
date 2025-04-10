package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
