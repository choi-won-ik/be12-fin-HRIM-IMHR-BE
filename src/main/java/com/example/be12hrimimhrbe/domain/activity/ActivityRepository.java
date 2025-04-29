package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {


    List<Activity> findAllByMember(Member member);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "where a.type!= 'EDUCATION' ")
    Page<Activity> findAllAndMemberNotEducation(Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "where m=:member " +
            "AND a.type!= 'EDUCATION' ")
    Page<Activity> findAllByMembernotEducation(Member member, PageRequest of);

    @EntityGraph(attributePaths = {"member","member.company"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "where a.idx= :idx ")
    Activity findByIdAndMember(Long idx);


    List<Activity> findAllByMemberIdx(Long memberIdx);

    @EntityGraph(attributePaths = {"member","member.company"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "where c.idx= :companyIdx " +
            "AND a.type= 'EDUCATION' ")
    Page<Activity> findAllAndMemberEducation(Long companyIdx, Pageable pageable);

    @EntityGraph(attributePaths = {"member","member.company"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "WHERE m=:member " +
            "And a.type= 'EDUCATION' ")
    Page<Activity> findAllByMemberEducation(Member member,Pageable pageable);
}
