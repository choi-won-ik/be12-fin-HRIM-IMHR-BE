package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByMember(Member member);

    @EntityGraph(attributePaths = {"activity","activity.member"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m ")
    List<Activity> findAllAndMember();
}
