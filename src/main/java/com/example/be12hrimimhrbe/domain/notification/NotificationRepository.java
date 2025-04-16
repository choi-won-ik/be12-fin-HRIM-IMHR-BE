package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMember(Member member);

//    @EntityGraph(attributePaths = {"member"})
//    @Query("SELECT n from Notification n " +
//            "LEFT JOIN n.member m " +
//            "where m=:member")
    Page<Notification> findAllByMember(Member member, Pageable of);
}
