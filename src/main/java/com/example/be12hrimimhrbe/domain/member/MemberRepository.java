package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberIdAndIsAdminAndStatus(String memberId, Boolean isAdmin, Member.Status status);
    Optional<Member> findByNameAndEmailAndIsAdmin(String name, String email, Boolean isAdmin);
    Optional<Member> findByMemberIdAndEmailAndIsAdmin(String memberId, String email, Boolean isAdmin);
    Optional<Member> findByEmailAndIsAdmin(String email, Boolean isAdmin);
    Optional<Member> findByMemberIdAndIsAdmin(String memberId, Boolean isAdmin);
    List<Member> findAllByCompany(Company company);
    List<Member> findAllByDepartmentAndIdxNotAndIsAdmin(Department department, Long idx, Boolean isAdmin);
    List<Member> findAllByDepartmentAndIsAdmin(Department department, Boolean isAdmin);

    Member findByIdx(Long idx);
}
