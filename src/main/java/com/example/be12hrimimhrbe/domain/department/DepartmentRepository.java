package com.example.be12hrimimhrbe.domain.department;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByCompany(Company company);

    Department findByIdx(Long idx);

    @Query("SELECT d, ds FROM Department d " +
            "LEFT JOIN DepartmentScore ds ON ds.department = d AND ds.year = :year AND ds.month = :month " +
            " WHERE d IN :d")
    List<Object[]> findAllDepartmentsWithScore(@Param("d") List<Department> departments, @Param("year") int year, @Param("month") int month);
}
