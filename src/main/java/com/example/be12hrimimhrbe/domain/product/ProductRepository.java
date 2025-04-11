package com.example.be12hrimimhrbe.domain.product;

import com.example.be12hrimimhrbe.domain.product.model.Product;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.product.model.ProductDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByMember(Member member);

    @EntityGraph(attributePaths = {"activity","activity.member"})
    @Query("SELECT a FROM Activity a " +
            "LEFT JOIN a.member m ")
    List<Activity> findAllAndMember();
}
