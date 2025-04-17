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
//    @EntityGraph(attributePaths = {"member"})
//    @Query("SELECT a FROM Activity a " +
//            "LEFT JOIN a.member m ")
    Page<Activity> findAllByMember(Member member, Pageable pageable);

    List<Activity> findAllByMember(Member member);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m ")
    Page<Activity> findAllAndMember(Pageable pageable);

    @EntityGraph(attributePaths = {"member","member.company"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m " +
            "LEFT JOIN m.company c " +
            "where a.idx= :idx")
    Activity findByIdAndMember(Long idx);

}
