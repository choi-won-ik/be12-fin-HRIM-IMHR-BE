package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentScoreRepository extends JpaRepository<DepartmentScore, Long> {

    @EntityGraph(attributePaths = {"department"})
    @Query("SELECT ds FROM DepartmentScore ds " +
            "LEFT JOIN ds.department d  " +
            "WHERE d.idx= :departmentIdx " +
            "AND ds.year=:year " +
            "AND ds.month=:month ")
    Optional<DepartmentScore> findByDepartmentIdx(Long departmentIdx, int year, int month);
}
