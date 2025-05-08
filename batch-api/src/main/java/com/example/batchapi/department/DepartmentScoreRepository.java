package com.example.batchapi.department;

import com.example.batchapi.department.model.DepartmentScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentScoreRepository extends JpaRepository<DepartmentScore, Long> {
}
