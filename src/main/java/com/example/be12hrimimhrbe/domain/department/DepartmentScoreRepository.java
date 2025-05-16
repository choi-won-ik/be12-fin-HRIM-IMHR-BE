package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentScoreRepository extends JpaRepository<DepartmentScore, Long> {

    @EntityGraph(attributePaths = {"department"})
    @Query("SELECT d, ds FROM Department d " +
            "LEFT JOIN DepartmentScore ds ON ds.department = d AND ds.year = :year AND ds.month = :month " +
            "WHERE d.idx = :departmentIdx")
    Object findByDepartmentIdx(@Param("departmentIdx") Long departmentIdx,
                                        @Param("year") int year,
                                        @Param("month") int month);
}
